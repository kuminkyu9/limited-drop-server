package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProduct_IdOrderBySortOrderAsc(Long productId);
}