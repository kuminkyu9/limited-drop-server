package com.github.kuminkyu9.limiteddropserver.service;

import com.github.kuminkyu9.limiteddropserver.entity.RaffleEntry;
import com.github.kuminkyu9.limiteddropserver.entity.RaffleEntryStatus;
import com.github.kuminkyu9.limiteddropserver.entity.RaffleProduct;
import com.github.kuminkyu9.limiteddropserver.entity.RaffleStatus;
import com.github.kuminkyu9.limiteddropserver.repository.RaffleEntryRepository;
import com.github.kuminkyu9.limiteddropserver.repository.RaffleProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RaffleDrawService {

    private final RaffleProductRepository raffleProductRepository;
    private final RaffleEntryRepository raffleEntryRepository;

    public void processPendingRaffles() {
        List<RaffleProduct> raffleProducts = raffleProductRepository.findByDrawCompletedFalse();
        LocalDateTime now = LocalDateTime.now();

        for (RaffleProduct raffleProduct : raffleProducts) {
            updateRaffleStatusByTime(raffleProduct, now);

            if (!raffleProduct.isDrawCompleted() && !now.isBefore(raffleProduct.getDrawAt())) {
                executeDraw(raffleProduct, now);
            }
        }
    }

    private void updateRaffleStatusByTime(RaffleProduct raffleProduct, LocalDateTime now) {
        if (now.isBefore(raffleProduct.getStartAt())) {
            raffleProduct.updateRaffleStatus(RaffleStatus.SCHEDULED);
            return;
        }

        if (now.isAfter(raffleProduct.getEndAt())) {
            raffleProduct.updateRaffleStatus(RaffleStatus.CLOSED);
            return;
        }

        raffleProduct.updateRaffleStatus(RaffleStatus.OPEN);
    }

    private void executeDraw(RaffleProduct raffleProduct, LocalDateTime now) {
        List<RaffleEntry> appliedEntries = raffleEntryRepository.findByRaffleProduct_IdAndStatus(
                raffleProduct.getId(),
                RaffleEntryStatus.APPLIED
        );

        if (appliedEntries.isEmpty()) {
            raffleProduct.updateRaffleStatus(RaffleStatus.CLOSED);
            raffleProduct.markDrawCompleted();
            return;
        }

        Collections.shuffle(appliedEntries);

        int winnerCount = Math.min(raffleProduct.getWinnerCount(), appliedEntries.size());

        for (int i = 0; i < appliedEntries.size(); i++) {
            RaffleEntry entry = appliedEntries.get(i);

            if (i < winnerCount) {
                entry.markWon(now);
            } else {
                entry.markLost(now);
            }
        }

        raffleProduct.updateRaffleStatus(RaffleStatus.CLOSED);
        raffleProduct.markDrawCompleted();
    }
}