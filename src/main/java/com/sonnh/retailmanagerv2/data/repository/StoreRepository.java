package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface StoreRepository extends JpaRepository<Store, UUID>, JpaSpecificationExecutor<Store> {

    @Query("SELECT s FROM Store s " +
            "JOIN Store_StoreInventory ssi ON s.id = ssi.store.id " +
            "JOIN ssi.promotionStoreStoreInventoryList p " +
            "WHERE ssi.storeInventory.id = :productId AND p.id = :promotionId AND s.audit.isActive = true ")
    List<Store> findStoreByProductIdAndPromotionId(UUID productId,UUID promotionId);
}