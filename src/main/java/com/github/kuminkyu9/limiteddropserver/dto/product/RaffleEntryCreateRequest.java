package com.github.kuminkyu9.limiteddropserver.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaffleEntryCreateRequest {

    @NotNull
    private Long productOptionId;
}