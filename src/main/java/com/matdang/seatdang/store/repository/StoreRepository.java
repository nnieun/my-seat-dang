package com.matdang.seatdang.store.repository;

import com.matdang.seatdang.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Page<Store> findByStoreNameContaining(String storeName, Pageable pageable);
}
