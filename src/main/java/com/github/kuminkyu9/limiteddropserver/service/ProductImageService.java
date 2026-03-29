package com.github.kuminkyu9.limiteddropserver.service;

import com.github.kuminkyu9.limiteddropserver.entity.Product;
import com.github.kuminkyu9.limiteddropserver.entity.ProductImage;
import com.github.kuminkyu9.limiteddropserver.entity.ProductImageType;
import com.github.kuminkyu9.limiteddropserver.repository.ProductImageRepository;
import com.github.kuminkyu9.limiteddropserver.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final S3Service s3Service;

    public Long uploadProductImage(
            Long productId,
            MultipartFile file,
            ProductImageType imageType,
            Integer sortOrder
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        String imageUrl = s3Service.uploadFile(file, "products");

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .imageType(imageType)
                .imageUrl(imageUrl)
                .sortOrder(sortOrder)
                .build();

        productImageRepository.save(productImage);

        return productImage.getId();
    }
}