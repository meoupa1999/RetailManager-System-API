package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.StoreImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface StoreImportRepository extends JpaRepository<StoreImport, UUID>, JpaSpecificationExecutor<StoreImport> {
}