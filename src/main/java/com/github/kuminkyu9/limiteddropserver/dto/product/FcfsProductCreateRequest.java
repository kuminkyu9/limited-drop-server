package com.github.kuminkyu9.limiteddropserver.dto.product;

import com.github.kuminkyu9.limiteddropserver.entity.ProductType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FcfsProductCreateRequest {

    @NotBlank
    private String brand;

    @NotBlank
    private String name;

    @NotNull
    @Min(0)
    private Integer price;

    @NotBlank
    private String description;

    @Valid
    @NotNull
    private FcfsDetailRequest fcfsDetail;

    @Valid
    @NotEmpty
    private List<ProductOptionCreateRequest> options;
}