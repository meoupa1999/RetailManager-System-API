package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, UUID>, JpaSpecificationExecutor<WarehouseInventory> {
//    @Query("SELECT wi FROM WarehouseInventory wi LEFT JOIN Warehouse_WarehouseInventory  wwi ON wi.id = wwi.warehouseInventory.id WHERE wwi.warehouse.id = :warehouseId OR wwi.warehouseInventory.id IS NULL")
//    List<WarehouseInventory> findAllProductAndQuantityWarehouse(UUID warehouseId);
}