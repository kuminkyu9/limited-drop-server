package com.github.kuminkyu9.limiteddropserver.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionCreateRequest {

    @NotBlank
    private String size;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    @NotNull
    @Min(0)
    private Integer soldQuantity;
}