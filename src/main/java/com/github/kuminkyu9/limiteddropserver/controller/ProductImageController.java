package com.github.kuminkyu9.limiteddropserver.controller;

import com.github.kuminkyu9.limiteddropserver.entity.ProductImageType;
import com.github.kuminkyu9.limiteddropserver.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/{productId}/images")
    public ResponseEntity<Long> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") ProductImageType imageType,
            @RequestParam("sortOrder") Integer sortOrder
    ) {
        Long imageId = productImageService.uploadProductImage(productId, file, imageType, sortOrder);
        return ResponseEntity.ok(imageId);
    }
}