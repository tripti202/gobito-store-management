
package com.gobito.store.dto;

import java.time.LocalDateTime;

import com.gobito.store.entity.StoreStatus;

public class StoreResponse {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private StoreStatus status;
    private LocalDateTime createdAt;

    public StoreResponse(
            Long id,
            String name,
            String address,
            String city,
            String state,
            String pincode,
            StoreStatus status,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public StoreStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
