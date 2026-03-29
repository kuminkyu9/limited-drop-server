package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.FcfsProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcfsProductRepository extends JpaRepository<FcfsProduct, Long> {
}