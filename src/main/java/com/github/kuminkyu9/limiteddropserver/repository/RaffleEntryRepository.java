package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.RaffleEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaffleEntryRepository extends JpaRepository<RaffleEntry, Long> {

    boolean existsByUser_IdAndRaffleProduct_Id(Long userId, Long raffleProductId);
}