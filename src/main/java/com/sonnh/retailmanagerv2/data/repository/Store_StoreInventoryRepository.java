package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.Warehouse_WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface Store_StoreInventoryRepository extends JpaRepository<Store_StoreInventory, UUID> {
    @Query("SELECT ssi FROM Store_StoreInventory  ssi " +
            "WHERE ssi.store.id = :storeId AND ssi.storeInventory.id = :productId")
    Store_StoreInventory findStoreStoreInventory(UUID storeId, UUID productId);
}