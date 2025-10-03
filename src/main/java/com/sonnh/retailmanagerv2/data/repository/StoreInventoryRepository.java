package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.WarehouseInventory;
import com.sonnh.retailmanagerv2.data.domain.Warehouse_WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface StoreInventoryRepository extends JpaRepository<StoreInventory, UUID> {
    @Query("SELECT si FROM StoreInventory si " +
            "WHERE si.productCode = :productCode AND si.audit.isActive = true")
    Optional<StoreInventory> findByProductCode(String productCode);
}