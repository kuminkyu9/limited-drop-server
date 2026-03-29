package com.github.kuminkyu9.limiteddropserver.dto.product;

import com.github.kuminkyu9.limiteddropserver.entity.FcfsSaleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class FcfsProductDetailResponse {

    private Long productId;
    private String brand;
    private String name;
    private Integer price;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer totalStock;
    private Integer soldQuantity;
    private FcfsSaleStatus saleStatus;
    private List<ProductOptionResponse> options;
    private List<ProductImageResponse> images;
}