package com.github.kuminkyu9.limiteddropserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RaffleDrawScheduler {

    private final RaffleDrawService raffleDrawService;

    @Scheduled(fixedDelay = 60000)
    public void run() {
        raffleDrawService.processPendingRaffles();
    }
}