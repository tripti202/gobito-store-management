
package com.gobito.store.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.gobito.store.dto.StoreUpdateRequest;

import com.gobito.store.dto.StoreRegistrationRequest;
import com.gobito.store.dto.StoreResponse;
import com.gobito.store.service.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

@GetMapping("/my-stores")

public ResponseEntity<List<StoreResponse>> getMyStores(
        Authentication authentication) {

    String ownerEmail = authentication.getName();

    List<StoreResponse> stores =
            storeService.getMyStores(ownerEmail);

    return ResponseEntity.ok(stores);
}


    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<StoreResponse> registerStore(
            @Valid @RequestBody StoreRegistrationRequest request,
            Authentication authentication) {

        String ownerEmail = authentication.getName();

        StoreResponse response =
                storeService.registerStore(request, ownerEmail);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

@PatchMapping("/{storeId}/deactivate")
public ResponseEntity<String> deactivateStore(
        @PathVariable Long storeId,
        Authentication authentication) {

    String ownerEmail = authentication.getName();

    storeService.deactivateStore(storeId, ownerEmail);

    return ResponseEntity.ok("Store deactivated successfully");
}


@PutMapping("/{storeId}")
public ResponseEntity<StoreResponse> updateStore(
        @PathVariable Long storeId,
        @Valid @RequestBody StoreUpdateRequest request,
        Authentication authentication) {

    String ownerEmail = authentication.getName();

    StoreResponse response = storeService.updateStore(
            storeId,
            request,
            ownerEmail
    );

    return ResponseEntity.ok(response);
}

    
}

