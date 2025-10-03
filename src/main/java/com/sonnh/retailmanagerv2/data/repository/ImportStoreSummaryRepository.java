package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.ImportStoreSummary;
import com.sonnh.retailmanagerv2.data.domain.Warehouse_WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ImportStoreSummaryRepository extends JpaRepository<ImportStoreSummary, UUID> {
    @Query("SELECT iss FROM ImportStoreSummary iss " +
            "WHERE iss.storeImportId = :storeImportId ")
    List<ImportStoreSummary> findImportStoreSummarisBy(UUID storeImportId);
}