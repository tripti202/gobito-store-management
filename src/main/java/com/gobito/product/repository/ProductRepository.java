package com.gobito.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobito.product.entity.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByStoreId(Long storeId);

    Optional<Product> findByIdAndStoreId(
            Long productId,
            Long storeId);
}