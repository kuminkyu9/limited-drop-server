package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.RaffleEntry;
import com.github.kuminkyu9.limiteddropserver.entity.RaffleEntryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaffleEntryRepository extends JpaRepository<RaffleEntry, Long> {

    boolean existsByUser_IdAndRaffleProduct_Id(Long userId, Long raffleProductId);

    List<RaffleEntry> findByRaffleProduct_IdAndStatus(Long raffleProductId, RaffleEntryStatus status);
}