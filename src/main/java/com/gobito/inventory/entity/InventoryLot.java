
package com.gobito.inventory.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gobito.product.entity.Product;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_lots")
public class InventoryLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public InventoryLot() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Available stock = total stock - reserved stock
    public Integer getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }

    // Add getters and setters for all fields

public Long getId() {
    return id;
}

public Product getProduct() {
    return product;
}

public void setProduct(Product product) {
    this.product = product;
}

public LocalDate getExpiryDate() {
    return expiryDate;
}

public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
}

public Integer getTotalQuantity() {
    return totalQuantity;
}

public void setTotalQuantity(Integer totalQuantity) {
    this.totalQuantity = totalQuantity;
}

public Integer getReservedQuantity() {
    return reservedQuantity;
}

public void setReservedQuantity(Integer reservedQuantity) {
    this.reservedQuantity = reservedQuantity;
}

public BigDecimal getSellingPrice() {
    return sellingPrice;
}

public void setSellingPrice(BigDecimal sellingPrice) {
    this.sellingPrice = sellingPrice;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

}
