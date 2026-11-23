package com.gobito.product.controller;
import com.gobito.product.dto.ProductUpdateRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


import com.gobito.product.dto.ProductRegistrationRequest;
import com.gobito.product.dto.ProductResponse;
import com.gobito.product.service.ProductService;

@RestController
@RequestMapping("/api/stores/{storeId}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getStoreProducts(
            @PathVariable Long storeId,
            Authentication authentication) {

        List<ProductResponse> products =
                productService.getStoreProducts(
                        storeId,
                        authentication.getName()
                );

        return ResponseEntity.ok(products);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request,
            Authentication authentication) {

        ProductResponse response = productService.updateProduct(
                storeId,
                productId,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{productId}/deactivate")
    public ResponseEntity<String> deactivateProduct(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            Authentication authentication) {

        productService.deactivateProduct(
                storeId,
                productId,
                authentication.getName()
        );

        return ResponseEntity.ok("Product deactivated successfully");
    }

    @PostMapping
    public ResponseEntity<ProductResponse> registerProduct(
            @PathVariable Long storeId,
            @Valid @RequestBody ProductRegistrationRequest request,
            Authentication authentication) {

        ProductResponse response = productService.registerProduct(
                storeId,
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}