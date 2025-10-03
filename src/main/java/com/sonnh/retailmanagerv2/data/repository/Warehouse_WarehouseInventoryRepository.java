package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.Warehouse_WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface Warehouse_WarehouseInventoryRepository extends JpaRepository<Warehouse_WarehouseInventory, UUID> {
    @Query("SELECT wwi FROM Warehouse_WarehouseInventory wwi " +
            "WHERE wwi.warehouse.id = :warehouseId AND wwi.warehouseInventory.id = :productId")
    Warehouse_WarehouseInventory findWarehouse_WarehouseInventories(UUID warehouseId, UUID productId);
}
