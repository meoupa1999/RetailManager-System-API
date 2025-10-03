package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.StoreImportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface StoreImportDetailRepository extends JpaRepository<StoreImportDetail, UUID>, JpaSpecificationExecutor<StoreImportDetail> {
}