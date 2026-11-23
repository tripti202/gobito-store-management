package com.gobito.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerCatalogueResponse {

    private Long lotId;
    private Long productId;
    private String productName;
    private String brand;
    private String description;
    private String category;
    private String unit;
    private BigDecimal regularPrice;
    private BigDecimal sellingPrice;
    private LocalDate expiryDate;
    private Integer availableQuantity;
    private Long storeId;
    private String storeName;
    private String city;

    public CustomerCatalogueResponse(
            Long lotId,
            Long productId,
            String productName,
            String brand,
            String description,
            String category,
            String unit,
            BigDecimal regularPrice,
            BigDecimal sellingPrice,
            LocalDate expiryDate,
            Integer availableQuantity,
            Long storeId,
            String storeName,
            String city) {

        this.lotId = lotId;
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.regularPrice = regularPrice;
        this.sellingPrice = sellingPrice;
        this.expiryDate = expiryDate;
        this.availableQuantity = availableQuantity;
        this.storeId = storeId;
        this.storeName = storeName;
        this.city = city;
    }

    public Long getLotId() { return lotId; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getBrand() { return brand; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getUnit() { return unit; }
    public BigDecimal getRegularPrice() { return regularPrice; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public Long getStoreId() { return storeId; }
    public String getStoreName() { return storeName; }
    public String getCity() { return city; }
}