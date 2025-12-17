package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface RankRepository extends JpaRepository<Rank, UUID>, JpaSpecificationExecutor<Rank> {
}