package com.github.kuminkyu9.limiteddropserver.dto.product;

import com.github.kuminkyu9.limiteddropserver.entity.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {

    @NotNull
    private ProductType productType;

    @NotBlank
    private String brand;

    @NotBlank
    private String name;

    @NotNull
    private Integer price;

    private String description;
}
