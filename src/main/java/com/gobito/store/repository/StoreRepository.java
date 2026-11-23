
package com.gobito.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobito.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByCityIgnoreCase(String city);

    List<Store> findByOwnerId(Long ownerId);

    List<Store> findByStatus(
            com.gobito.store.entity.StoreStatus status);
}

