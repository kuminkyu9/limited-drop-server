package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}