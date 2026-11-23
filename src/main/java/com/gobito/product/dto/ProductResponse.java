package com.gobito.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private String category;
    private String unit;
    private BigDecimal regularPrice;
    private Long storeId;
    private LocalDateTime createdAt;

    public ProductResponse(
            Long id,
            String name,
            String brand,
            String description,
            String category,
            String unit,
            BigDecimal regularPrice,
            Long storeId,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.regularPrice = regularPrice;
        this.storeId = storeId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getUnit() { return unit; }
    public BigDecimal getRegularPrice() { return regularPrice; }
    public Long getStoreId() { return storeId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}