
package com.gobito.inventory.repository;

import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobito.inventory.entity.InventoryLot;

public interface InventoryLotRepository
        extends JpaRepository<InventoryLot, Long> {

    List<InventoryLot> findByProductId(Long productId);
    Optional<InventoryLot> findByIdAndProductId(
            Long id,
            Long productId);
    @Query("""
    	    SELECT lot FROM InventoryLot lot
    	    JOIN lot.product product
    	    JOIN product.store store
    	    WHERE store.status = 'ACTIVE'
    	      AND product.active = true
    	      AND lot.expiryDate >= :today
    	      AND lot.totalQuantity > lot.reservedQuantity
    	    ORDER BY lot.expiryDate ASC
    	""")
    	List<InventoryLot> findAvailableCatalogueLots(
    	        @Param("today") LocalDate today);
}
