package com.gobito.product.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gobito.product.dto.ProductUpdateRequest;

import com.gobito.product.dto.ProductRegistrationRequest;
import com.gobito.product.dto.ProductResponse;
import com.gobito.product.entity.Product;
import com.gobito.product.repository.ProductRepository;
import com.gobito.store.entity.Store;
import com.gobito.store.entity.StoreStatus;
import com.gobito.store.repository.StoreRepository;
import com.gobito.user.entity.Role;
import com.gobito.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import com.gobito.user.repository.UserRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public ProductService(
            ProductRepository productRepository,
            StoreRepository storeRepository,
            UserRepository userRepository) {

        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void deactivateProduct(
            Long storeId,
            Long productId,
            String ownerEmail) {

        // 1. Find logged-in owner
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        // 2. Verify role
        if (owner.getRole() != Role.STORE_OWNER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only store owners can deactivate products");
        }

        // 3. Find store
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Store not found"));

        // 4. Verify store ownership
        if (!store.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot deactivate products of another owner's store");
        }

        // 5. Find product belonging to this store
        Product product = productRepository
                .findByIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));

        // 6. Check whether already inactive
        if (!product.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Product is already inactive");
        }

        // 7. Soft delete
        product.setActive(false);

        productRepository.save(product);
    }
    @Transactional(readOnly = true)
    public List<ProductResponse> getStoreProducts(
            Long storeId,
            String ownerEmail) {

        // 1. Find logged-in owner
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        // 2. Verify role
        if (owner.getRole() != Role.STORE_OWNER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only store owners can view their store products");
        }

        // 3. Find store
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Store not found"));

        // 4. Verify ownership
        if (!store.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot view another owner's products");
        }

        // 5. Fetch products belonging to this store
        List<Product> products =
                productRepository.findByStoreId(storeId);

        // 6. Convert entities into response DTOs
        return products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getBrand(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getUnit(),
                        product.getRegularPrice(),
                        store.getId(),
                        product.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ProductResponse updateProduct(
            Long storeId,
            Long productId,
            ProductUpdateRequest request,
            String ownerEmail) {

        // 1. Find logged-in owner
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        // 2. Verify role
        if (owner.getRole() != Role.STORE_OWNER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only store owners can update products");
        }

        // 3. Find store
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Store not found"));

        // 4. Verify store ownership
        if (!store.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot update products of another owner's store");
        }

        // 5. Find product belonging to this store
        Product product = productRepository
                .findByIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));

        // 6. Update permitted fields
        product.setName(request.getName().trim());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory().trim());
        product.setUnit(request.getUnit().trim());
        product.setRegularPrice(request.getRegularPrice());

        // 7. Save updated product
        Product updatedProduct = productRepository.save(product);

        // 8. Return response
        return new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getBrand(),
                updatedProduct.getDescription(),
                updatedProduct.getCategory(),
                updatedProduct.getUnit(),
                updatedProduct.getRegularPrice(),
                store.getId(),
                updatedProduct.getCreatedAt()
        );
    }

    @Transactional
    public ProductResponse registerProduct(
            Long storeId,
            ProductRegistrationRequest request,
            String ownerEmail) {

        // 1. Find the logged-in owner
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        // 2. Verify the user's role
        if (owner.getRole() != Role.STORE_OWNER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only store owners can add products");
        }

        // 3. Find the requested store
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Store not found"));

        // 4. Verify that this store belongs to the logged-in owner
        if (!store.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot add products to another owner's store");
        }

        // 5. Only active stores can add products
        if (store.getStatus() != StoreStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Store must be active before adding products");
        }

        // 6. Create product
        Product product = new Product();

        product.setName(request.getName().trim());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory().trim());
        product.setUnit(request.getUnit().trim());
        product.setRegularPrice(request.getRegularPrice());
        product.setStore(store);

        // 7. Save product
        Product savedProduct = productRepository.save(product);

        // 8. Return response DTO
        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getBrand(),
                savedProduct.getDescription(),
                savedProduct.getCategory(),
                savedProduct.getUnit(),
                savedProduct.getRegularPrice(),
                store.getId(),
                savedProduct.getCreatedAt()
        );
    }
}
