package com.github.kuminkyu9.limiteddropserver.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FcfsDetailRequest {

    @NotNull
    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @NotNull
    @Min(0)
    private Integer totalStock;

    @NotNull
    @Min(0)
    private Integer saleQuantity;
}