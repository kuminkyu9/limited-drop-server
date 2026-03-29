package com.github.kuminkyu9.limiteddropserver.controller;

import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductDetailResponse;
import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductListResponse;
import com.github.kuminkyu9.limiteddropserver.config.AuthUser;
import com.github.kuminkyu9.limiteddropserver.dto.product.FcfsProductCreateRequest;
import com.github.kuminkyu9.limiteddropserver.dto.product.ProductCreateRequest;
import com.github.kuminkyu9.limiteddropserver.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 기본 골조(상품)만 만듬
    @PostMapping("/register")
    public ResponseEntity<Long> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            Authentication authentication
    ) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        Long productId = productService.createProduct(request, authUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

    // 기본 골조(상품) 및 상품 옵션과 선착순 상품 까지 다 만듬
    @PostMapping("/fcfs")
    public ResponseEntity<Long> createFcfsProduct(
            @Valid @RequestBody FcfsProductCreateRequest request,
            Authentication authentication
    ) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        Long productId = productService.createFcfsProduct(request, authUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

    // 선착순 상품 가져오기 (페이지)
    @GetMapping("/fcfs")
    public ResponseEntity<Page<FcfsProductListResponse>> getFcfsProducts(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<FcfsProductListResponse> response = productService.getFcfsProducts(pageable);
        return ResponseEntity.ok(response);
    }

    // 선착순 상품 상세 정보 가져오기
    @GetMapping("/fcfs/{productId}")
    public ResponseEntity<FcfsProductDetailResponse> getFcfsProductDetail(
            @PathVariable Long productId
    ) {
        FcfsProductDetailResponse response = productService.getFcfsProductDetail(productId);
        return ResponseEntity.ok(response);
    }
}
