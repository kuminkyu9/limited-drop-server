package com.github.kuminkyu9.limiteddropserver.service;

import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductOptionCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductDetailResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductListResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductOptionResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductImageResponse;
import com.github.kuminkyu9.limiteddropserver.entity.*;
import com.github.kuminkyu9.limiteddropserver.repository.FcfsProductRepository;
import com.github.kuminkyu9.limiteddropserver.repository.ProductOptionRepository;
import com.github.kuminkyu9.limiteddropserver.repository.ProductRepository;
import com.github.kuminkyu9.limiteddropserver.repository.UserRepository;
import com.github.kuminkyu9.limiteddropserver.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    .soldQuantity(optionRequest.getSoldQuantity())
                    .build();

            productOptionRepository.save(option);
        }

        return savedProduct.getId();
    }

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
}
