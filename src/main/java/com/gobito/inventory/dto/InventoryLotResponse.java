
package com.gobito.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryLotResponse {

    private Long id;
    private Long productId;
    private String productName;
    private LocalDate expiryDate;
    private Integer totalQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private BigDecimal sellingPrice;
    private LocalDateTime createdAt;

    public InventoryLotResponse(
            Long id,
            Long productId,
            String productName,
            LocalDate expiryDate,
            Integer totalQuantity,
            Integer reservedQuantity,
            Integer availableQuantity,
            BigDecimal sellingPrice,
            LocalDateTime createdAt) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.expiryDate = expiryDate;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
        this.sellingPrice = sellingPrice;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public Integer getReservedQuantity() { return reservedQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

