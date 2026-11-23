
package com.gobito.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.gobito.inventory.dto.InventoryLotUpdateRequest;

import com.gobito.inventory.dto.InventoryLotRegistrationRequest;
import com.gobito.inventory.dto.InventoryLotResponse;
import com.gobito.inventory.service.InventoryLotService;

@RestController
@RequestMapping("/api/stores/{storeId}/products/{productId}/inventory")
public class InventoryLotController {

    private final InventoryLotService inventoryLotService;

    public InventoryLotController(
            InventoryLotService inventoryLotService) {
        this.inventoryLotService = inventoryLotService;
    }

    // Add inventory lot
    @PostMapping
    public ResponseEntity<InventoryLotResponse> addInventoryLot(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @Valid @RequestBody InventoryLotRegistrationRequest request,
            Authentication authentication) {

        InventoryLotResponse response =
                inventoryLotService.addInventoryLot(
                        storeId,
                        productId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all inventory lots for a product
    @GetMapping
    public ResponseEntity<List<InventoryLotResponse>> getInventoryLots(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            Authentication authentication) {

        List<InventoryLotResponse> lots =
                inventoryLotService.getInventoryLots(
                        storeId,
                        productId,
                        authentication.getName()
                );

        return ResponseEntity.ok(lots);
    }

@PutMapping("/{lotId}")
public ResponseEntity<InventoryLotResponse> updateInventoryLot(
        @PathVariable Long storeId,
        @PathVariable Long productId,
        @PathVariable Long lotId,
        @Valid @RequestBody InventoryLotUpdateRequest request,
        Authentication authentication) {

    InventoryLotResponse response =
            inventoryLotService.updateInventoryLot(
                    storeId,
                    productId,
                    lotId,
                    request,
                    authentication.getName()
            );

    return ResponseEntity.ok(response);
}

}


