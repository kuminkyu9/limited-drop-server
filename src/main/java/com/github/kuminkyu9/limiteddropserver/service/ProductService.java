package com.github.kuminkyu9.limiteddropserver.service;


import com.github.kuminkyu9.limiteddropserver.dto.product.ProductOptionCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductOptionResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductImageResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductDetailResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductListResponse;

import com.github.kuminkyu9.limiteddropserver.dto.product.RaffleProductCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.RaffleDetailRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.RaffleProductDetailResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.RaffleProductListResponse;

import com.github.kuminkyu9.limiteddropserver.entity.*;
import com.github.kuminkyu9.limiteddropserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Long createProduct(ProductCreateRequest request, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Product product = Product.builder()
                .seller(seller)
                .productType(request.getProductType())
                .brand(request.getBrand())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    // aws s3 이미지
    private final ProductImageRepository productImageRepository;

    private List<ProductImageResponse> getProductImages(Long productId) {
        return productImageRepository.findByProduct_IdOrderBySortOrderAsc(productId)
                .stream()
                .map(image -> new ProductImageResponse(
                        image.getId(),
                        image.getProduct().getId(),
                        image.getImageType(),
                        image.getImageUrl(),
                        image.getSortOrder(),
                        image.getCreatedAt()
                ))
                .toList();
    }

    // 선착순 상품 관련
    private final ProductOptionRepository productOptionRepository;
    private final FcfsProductRepository fcfsProductRepository;

    public Long createFcfsProduct(FcfsProductCreateRequest request, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Product product = Product.builder()
                .seller(seller)
                .productType(ProductType.FCFS)
                .brand(request.getBrand())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        Product savedProduct = productRepository.save(product);

        FcfsProduct fcfsProduct = FcfsProduct.builder()
                .product(savedProduct)
                .startAt(request.getFcfsDetail().getStartAt())
                .endAt(request.getFcfsDetail().getEndAt())
                .totalStock(request.getFcfsDetail().getTotalStock())
                .soldQuantity(0)
                .saleStatus(FcfsSaleStatus.SCHEDULED)
                .build();

        fcfsProductRepository.save(fcfsProduct);

        for (ProductOptionCreateRequest optionRequest : request.getOptions()) {
            ProductOption option = ProductOption.builder()
                    .product(savedProduct)
                    .size(optionRequest.getSize())
                    .stockQuantity(optionRequest.getStockQuantity())
                    .soldQuantity(0)
                    .build();

            productOptionRepository.save(option);
        }

        return savedProduct.getId();
    }

    @Transactional(readOnly = true)
    public Page<FcfsProductListResponse> getFcfsProducts(Pageable pageable) {
        return fcfsProductRepository.findAll(pageable)
                .map(fcfsProduct -> {
                    Long productId = fcfsProduct.getProduct().getId();

                    List<ProductImageResponse> images = getProductImages(productId);

                    return new FcfsProductListResponse(
                            fcfsProduct.getProduct().getId(),
                            fcfsProduct.getProduct().getBrand(),
                            fcfsProduct.getProduct().getName(),
                            fcfsProduct.getProduct().getPrice(),
                            fcfsProduct.getStartAt(),
                            fcfsProduct.getSaleStatus(),
                            images
                    );
                });
    }

    @Transactional(readOnly = true)
    public FcfsProductDetailResponse getFcfsProductDetail(Long productId) {
        FcfsProduct fcfsProduct = fcfsProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("선착순 상품을 찾을 수 없습니다."));

        List<ProductOptionResponse> options = productOptionRepository.findByProduct_Id(productId)
                .stream()
                .map(option -> new ProductOptionResponse(
                        option.getId(),
                        option.getSize(),
                        option.getStockQuantity(),
                        option.getSoldQuantity()
                ))
                .toList();

        List<ProductImageResponse> images = getProductImages(productId);

        return new FcfsProductDetailResponse(
                fcfsProduct.getProduct().getId(),
                fcfsProduct.getProduct().getBrand(),
                fcfsProduct.getProduct().getName(),
                fcfsProduct.getProduct().getPrice(),
                fcfsProduct.getProduct().getDescription(),
                fcfsProduct.getStartAt(),
                fcfsProduct.getEndAt(),
                fcfsProduct.getTotalStock(),
                fcfsProduct.getSoldQuantity(),
                fcfsProduct.getSaleStatus(),
                options,
                images
        );
    }

    // 추첨 상품 관련
    private void validateRaffleDetail(RaffleDetailRequest detail) {
        if (!detail.getStartAt().isBefore(detail.getEndAt())) {
            throw new IllegalArgumentException("추첨 시작 시간은 종료 시간보다 빨라야 합니다.");
        }

        if (detail.getEndAt().isAfter(detail.getDrawAt())) {
            throw new IllegalArgumentException("추첨 종료 시간은 발표 시간보다 늦을 수 없습니다.");
        }

        if (detail.getWinnerCount() < 1) {
            throw new IllegalArgumentException("당첨자 수는 1명 이상이어야 합니다.");
        }
    }

    private RaffleStatus calculateRaffleStatus(LocalDateTime startAt, LocalDateTime endAt) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startAt)) {
            return RaffleStatus.SCHEDULED;
        }

        if (now.isAfter(endAt)) {
            return RaffleStatus.CLOSED;
        }

        return RaffleStatus.OPEN;
    }

    private final RaffleProductRepository raffleProductRepository;

    public Long createRaffleProduct(RaffleProductCreateRequest request, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (seller.getRole() != UserRole.SELLER) {
            throw new IllegalArgumentException("판매자만 추첨 상품을 등록할 수 있습니다.");
        }

        validateRaffleDetail(request.getRaffleDetail());

        Product product = Product.builder()
                .seller(seller)
                .productType(ProductType.RAFFLE)
                .brand(request.getBrand())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        Product savedProduct = productRepository.save(product);

        RaffleProduct raffleProduct = RaffleProduct.builder()
                .product(savedProduct)
                .startAt(request.getRaffleDetail().getStartAt())
                .endAt(request.getRaffleDetail().getEndAt())
                .drawAt(request.getRaffleDetail().getDrawAt())
                .winnerCount(request.getRaffleDetail().getWinnerCount())
                .raffleStatus(calculateRaffleStatus(
                        request.getRaffleDetail().getStartAt(),
                        request.getRaffleDetail().getEndAt()
                ))
                .build();

        raffleProductRepository.save(raffleProduct);

        for (ProductOptionCreateRequest optionRequest : request.getOptions()) {
            ProductOption option = ProductOption.builder()
                    .product(savedProduct)
                    .size(optionRequest.getSize())
                    .stockQuantity(optionRequest.getStockQuantity())
                    .soldQuantity(0)
                    .build();

            productOptionRepository.save(option);
        }

        return savedProduct.getId();
    }

    @Transactional(readOnly = true)
    public Page<RaffleProductListResponse> getRaffleProducts(Pageable pageable) {
        return raffleProductRepository.findAll(pageable)
                .map(raffleProduct -> {
                    Long productId = raffleProduct.getProduct().getId();

                    List<ProductImageResponse> images = getProductImages(productId);

                    return new RaffleProductListResponse(
                            productId,
                            raffleProduct.getProduct().getBrand(),
                            raffleProduct.getProduct().getName(),
                            raffleProduct.getProduct().getPrice(),
                            raffleProduct.getStartAt(),
                            raffleProduct.getEndAt(),
                            raffleProduct.getDrawAt(),
                            raffleProduct.getRaffleStatus(),
                            images
                    );
                });
    }

    @Transactional(readOnly = true)
    public RaffleProductDetailResponse getRaffleProductDetail(Long productId) {
        RaffleProduct raffleProduct = raffleProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("추첨 상품을 찾을 수 없습니다."));

        List<ProductOptionResponse> options = productOptionRepository.findByProduct_Id(productId)
                .stream()
                .map(option -> new ProductOptionResponse(
                        option.getId(),
                        option.getSize(),
                        option.getStockQuantity(),
                        option.getSoldQuantity()
                ))
                .toList();

        List<ProductImageResponse> images = getProductImages(productId);

        return new RaffleProductDetailResponse(
                raffleProduct.getProduct().getId(),
                raffleProduct.getProduct().getBrand(),
                raffleProduct.getProduct().getName(),
                raffleProduct.getProduct().getPrice(),
                raffleProduct.getProduct().getDescription(),
                raffleProduct.getStartAt(),
                raffleProduct.getEndAt(),
                raffleProduct.getDrawAt(),
                raffleProduct.getWinnerCount(),
                raffleProduct.getRaffleStatus(),
                options,
                images
        );
    }
}
