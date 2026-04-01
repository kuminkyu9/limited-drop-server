package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.RaffleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaffleProductRepository extends JpaRepository<RaffleProduct, Long> {

    List<RaffleProduct> findByDrawCompletedFalse();
}