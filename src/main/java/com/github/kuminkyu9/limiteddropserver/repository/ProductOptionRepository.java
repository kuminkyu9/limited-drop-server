package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProduct_Id(Long productId);
}