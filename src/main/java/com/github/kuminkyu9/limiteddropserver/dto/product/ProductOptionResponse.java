package com.github.kuminkyu9.limiteddropserver.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionResponse {

    private Long optionId;
    private String size;
    private Integer stockQuantity;
    private Integer soldQuantity;
}