package com.github.kuminkyu9.limiteddropserver.dto.product;

import com.github.kuminkyu9.limiteddropserver.entity.RaffleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RaffleProductListResponse {

    private Long productId;
    private String brand;
    private String name;
    private Integer price;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime drawAt;
    private RaffleStatus raffleStatus;
    private List<ProductImageResponse> images;
}