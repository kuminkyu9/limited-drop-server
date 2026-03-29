package com.github.kuminkyu9.limiteddropserver.dto.product;

import com.github.kuminkyu9.limiteddropserver.entity.ProductImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductImageResponse {

    private Long imageId;   // PK
    private Long productId; // FK
    private ProductImageType imageType; // MAIN, DETAIL
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}