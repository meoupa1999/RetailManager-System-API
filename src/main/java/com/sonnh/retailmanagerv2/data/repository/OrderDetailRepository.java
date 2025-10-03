package com.sonnh.retailmanagerv2.data.repository;

import com.sonnh.retailmanagerv2.data.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
}