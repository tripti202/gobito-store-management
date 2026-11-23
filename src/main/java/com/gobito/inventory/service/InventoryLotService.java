
package com.gobito.inventory.service;


import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import com.gobito.inventory.dto.CustomerCatalogueResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gobito.inventory.repository.InventoryLotRepository;
import com.gobito.inventory.entity.InventoryLot;
import com.gobito.inventory.dto.InventoryLotRegistrationRequest;
import com.gobito.inventory.dto.InventoryLotUpdateRequest;
import com.gobito.inventory.dto.InventoryLotResponse;

import com.gobito.product.entity.Product;
import com.gobito.product.repository.ProductRepository;

import com.gobito.store.entity.Store;
import com.gobito.store.entity.StoreStatus;
import com.gobito.store.repository.StoreRepository;

import com.gobito.user.entity.User;
import com.gobito.user.entity.Role;
import com.gobito.user.repository.UserRepository;


@Service
public class InventoryLotService {

    private final InventoryLotRepository inventoryLotRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public InventoryLotService(
            InventoryLotRepository inventoryLotRepository,
            ProductRepository productRepository,
            StoreRepository storeRepository,
            UserRepository userRepository) {

        this.inventoryLotRepository = inventoryLotRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

@Transactional
public InventoryLotResponse addInventoryLot(
        Long storeId,
        Long productId,
        InventoryLotRegistrationRequest request,
        String ownerEmail) {

    // 1. Find the logged-in user
    User owner = userRepository.findByEmail(ownerEmail)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // 2. Verify that the user is a store owner
    if (owner.getRole() != Role.STORE_OWNER) {
        throw new org.springframework.security.access.AccessDeniedException(
                "Only store owners can manage inventory");
    }

    // 3. Find the store
    Store store = storeRepository.findById(storeId)
            .orElseThrow(() ->
                    new RuntimeException("Store not found"));

    // 4. Verify store ownership
    if (!store.getOwner().getId().equals(owner.getId())) {
        throw new org.springframework.security.access.AccessDeniedException(
                "You cannot manage inventory of another owner's store");
    }

    // 5. Verify that the store is active
    if (store.getStatus() != StoreStatus.ACTIVE) {
        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Store is not active");
    }

    // 6. Find the product and verify it belongs to this store
    Product product = productRepository
            .findByIdAndStoreId(productId, storeId)
            .orElseThrow(() ->
                    new RuntimeException("Product not found in this store"));

    // 7. Verify product is active
    if (!product.isActive()) {
        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Cannot add inventory to an inactive product");
    }

    // 8. Create the inventory lot
    InventoryLot lot = new InventoryLot();

    lot.setProduct(product);
    lot.setExpiryDate(request.getExpiryDate());
    lot.setTotalQuantity(request.getTotalQuantity());
    lot.setReservedQuantity(0);
    lot.setSellingPrice(request.getSellingPrice());

    // 9. Save the lot
    InventoryLot savedLot = inventoryLotRepository.save(lot);

    // 10. Return response
    return new InventoryLotResponse(
            savedLot.getId(),
            product.getId(),
            product.getName(),
            savedLot.getExpiryDate(),
            savedLot.getTotalQuantity(),
            savedLot.getReservedQuantity(),
            savedLot.getAvailableQuantity(),
            savedLot.getSellingPrice(),
            savedLot.getCreatedAt()
    );
}


@Transactional(readOnly = true)
public java.util.List<InventoryLotResponse> getInventoryLots(
        Long storeId,
        Long productId,
        String ownerEmail) {

    User owner = userRepository.findByEmail(ownerEmail)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (owner.getRole() != Role.STORE_OWNER) {
        throw new org.springframework.security.access.AccessDeniedException(
                "Only store owners can view inventory");
    }

    Store store = storeRepository.findById(storeId)
            .orElseThrow(() ->
                    new RuntimeException("Store not found"));

    if (!store.getOwner().getId().equals(owner.getId())) {
        throw new org.springframework.security.access.AccessDeniedException(
                "You cannot view another owner's inventory");
    }

    Product product = productRepository
            .findByIdAndStoreId(productId, storeId)
            .orElseThrow(() ->
                    new RuntimeException("Product not found in this store"));

    return inventoryLotRepository.findByProductId(productId)
            .stream()
            .map(lot -> new InventoryLotResponse(
                    lot.getId(),
                    product.getId(),
                    product.getName(),
                    lot.getExpiryDate(),
                    lot.getTotalQuantity(),
                    lot.getReservedQuantity(),
                    lot.getAvailableQuantity(),
                    lot.getSellingPrice(),
                    lot.getCreatedAt()
            ))
            .toList();
}


@Transactional
public InventoryLotResponse updateInventoryLot(
        Long storeId,
        Long productId,
        Long lotId,
        InventoryLotUpdateRequest request,
        String ownerEmail) {

    // 1. Find owner
    User owner = userRepository.findByEmail(ownerEmail)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found"));

    // 2. Verify role
    if (owner.getRole() != Role.STORE_OWNER) {
        throw new AccessDeniedException(
                "Only store owners can update inventory");
    }

    // 3. Find store
    Store store = storeRepository.findById(storeId)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Store not found"));

    // 4. Verify ownership
    if (!store.getOwner().getId().equals(owner.getId())) {
        throw new AccessDeniedException(
                "You cannot update another owner's inventory");
    }

    // 5. Find product
    Product product = productRepository
            .findByIdAndStoreId(productId, storeId)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Product not found in this store"));

    // 6. Find inventory lot belonging to this product
    InventoryLot lot = inventoryLotRepository
            .findByIdAndProductId(lotId, productId)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Inventory lot not found"));

    // 7. Prevent reducing quantity below reserved stock
    if (request.getTotalQuantity() < lot.getReservedQuantity()) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Total quantity cannot be less than reserved quantity");
    }

    // 8. Update fields
    lot.setExpiryDate(request.getExpiryDate());
    lot.setTotalQuantity(request.getTotalQuantity());
    lot.setSellingPrice(request.getSellingPrice());

    // 9. Save
    InventoryLot updatedLot = inventoryLotRepository.save(lot);

    // 10. Return response
    return mapToResponse(updatedLot);
}

private InventoryLotResponse mapToResponse(InventoryLot lot) {

    Product product = lot.getProduct();

    return new InventoryLotResponse(
            lot.getId(),
            product.getId(),
            product.getName(),
            lot.getExpiryDate(),
            lot.getTotalQuantity(),
            lot.getReservedQuantity(),
            lot.getAvailableQuantity(),
            lot.getSellingPrice(),
            lot.getCreatedAt()
    );
}
@Transactional(readOnly = true)
public List<CustomerCatalogueResponse> getCustomerCatalogue() {

    List<InventoryLot> lots =
            inventoryLotRepository.findAvailableCatalogueLots(
                    LocalDate.now()
            );

    return lots.stream()
            .map(lot -> {

                Product product = lot.getProduct();
                Store store = product.getStore();

                return new CustomerCatalogueResponse(
                        lot.getId(),
                        product.getId(),
                        product.getName(),
                        product.getBrand(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getUnit(),
                        product.getRegularPrice(),
                        lot.getSellingPrice(),
                        lot.getExpiryDate(),
                        lot.getAvailableQuantity(),
                        store.getId(),
                        store.getName(),
                        store.getCity()
                );
            })
            .toList();
}

}
