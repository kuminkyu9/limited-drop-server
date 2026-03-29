package com.github.kuminkyu9.limiteddropserver.dto.product;

import com.github.kuminkyu9.limiteddropserver.entity.FcfsSaleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class FcfsProductListResponse {

    private Long productId;
    private String brand;
    private String name;
    private Integer price;
    private LocalDateTime startAt;
    private FcfsSaleStatus saleStatus;
    private List<ProductImageResponse> images;
}