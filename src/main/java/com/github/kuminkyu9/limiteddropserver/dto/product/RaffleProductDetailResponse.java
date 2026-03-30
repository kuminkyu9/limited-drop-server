package com.github.kuminkyu9.limiteddropserver.dto.product;

import com.github.kuminkyu9.limiteddropserver.entity.RaffleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RaffleProductDetailResponse {

    private Long productId;
    private String brand;
    private String name;
    private Integer price;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime drawAt;
    private Integer winnerCount;
    private RaffleStatus raffleStatus;
    private List<ProductOptionResponse> options;
    private List<ProductImageResponse> images;
}