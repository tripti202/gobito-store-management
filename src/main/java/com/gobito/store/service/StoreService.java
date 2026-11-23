
package com.gobito.store.service;
import com.gobito.store.entity.StoreStatus;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobito.store.dto.StoreRegistrationRequest;
import com.gobito.store.dto.StoreResponse;
import com.gobito.store.entity.Store;
import java.util.List;
import java.util.stream.Collectors;
import com.gobito.store.repository.StoreRepository;
import com.gobito.user.entity.Role;
import com.gobito.user.entity.User;
import com.gobito.user.repository.UserRepository;
import com.gobito.store.dto.StoreUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreService(
            StoreRepository storeRepository,
            UserRepository userRepository) {

        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }
    public List<StoreResponse> getMyStores(String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (owner.getRole() != Role.STORE_OWNER) {
            throw new AccessDeniedException(
                    "Only store owners can view their stores");
        }

        return storeRepository.findByOwnerId(owner.getId())
                .stream()
                .map(store -> new StoreResponse(
                        store.getId(),
                        store.getName(),
                        store.getAddress(),
                        store.getCity(),
                        store.getState(),
                        store.getPincode(),
                        store.getStatus(),
                        store.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

@Transactional
public void deactivateStore(Long storeId, String ownerEmail) {

    // 1. Find the logged-in owner
    User owner = userRepository.findByEmail(ownerEmail)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // 2. Verify role
    if (owner.getRole() != Role.STORE_OWNER) {
        throw new AccessDeniedException(
                "Only store owners can deactivate stores");
    }

    // 3. Find the store
    Store store = storeRepository.findById(storeId)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Store not found"));

    // 4. Verify ownership
    if (!store.getOwner().getId().equals(owner.getId())) {
        throw new AccessDeniedException(
                "You cannot deactivate another owner's store");
    }

    // 5. Prevent deactivation if already inactive
    if (store.getStatus() == StoreStatus.INACTIVE) {
        throw new IllegalStateException(
                "Store is already inactive");
    }

    // 6. Change status
    store.setStatus(StoreStatus.INACTIVE);

    storeRepository.save(store);
}


@Transactional
public StoreResponse updateStore(
        Long storeId,
        StoreUpdateRequest request,
        String ownerEmail) {

    // 1. Find the logged-in owner
    User owner = userRepository.findByEmail(ownerEmail)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // 2. Verify role
    if (owner.getRole() != Role.STORE_OWNER) {
        throw new AccessDeniedException(
                "Only store owners can update stores");
    }

    // 3. Find the store
    Store store = storeRepository.findById(storeId)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Store not found"));

    // 4. Verify ownership
    if (!store.getOwner().getId().equals(owner.getId())) {
        throw new AccessDeniedException(
                "You cannot update another owner's store");
    }

    // 5. Update permitted fields
    store.setName(request.getName().trim());
    store.setDescription(request.getDescription());
    store.setAddress(request.getAddress().trim());
    store.setCity(request.getCity().trim());
    store.setState(request.getState().trim());
    store.setPincode(request.getPincode());

    // 6. Save updated store
    Store updatedStore = storeRepository.save(store);

    // 7. Return response
    return new StoreResponse(
            updatedStore.getId(),
            updatedStore.getName(),
            updatedStore.getAddress(),
            updatedStore.getCity(),
            updatedStore.getState(),
            updatedStore.getPincode(),
            updatedStore.getStatus(),
            updatedStore.getCreatedAt()
    );
}


    @Transactional
    public StoreResponse registerStore(
            StoreRegistrationRequest request,
            String ownerEmail) {

        // Find the logged-in owner
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Verify the user's role
        if (owner.getRole() != Role.STORE_OWNER) {
            throw new AccessDeniedException(
                    "Only store owners can register stores");
        }

        // Create the store
        Store store = new Store();

        store.setName(request.getName().trim());
        store.setDescription(request.getDescription());
        store.setAddress(request.getAddress().trim());
        store.setCity(request.getCity().trim());
        store.setState(request.getState().trim());
        store.setPincode(request.getPincode());

        // Set owner from authenticated user, not request
        store.setOwner(owner);

        // Save store
        // Default status is PENDING_APPROVAL
        Store savedStore = storeRepository.save(store);

        // Convert entity into response DTO
        return new StoreResponse(
                savedStore.getId(),
                savedStore.getName(),
                savedStore.getAddress(),
                savedStore.getCity(),
                savedStore.getState(),
                savedStore.getPincode(),
                savedStore.getStatus(),
                savedStore.getCreatedAt()
        );
    }
}

