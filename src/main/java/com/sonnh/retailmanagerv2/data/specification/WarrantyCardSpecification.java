package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.*;
import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import com.sonnh.retailmanagerv2.data.domain.enums.OrderStatus;
import com.sonnh.retailmanagerv2.security.StoreContextDetail;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.UUID;

public class WarrantyCardSpecification {

    public static Specification<WarrantyCard> hasStoreId() {
        return (root, query, cb) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            StoreContextDetail context = (StoreContextDetail) auth.getDetails();
            UUID storeId = context.getStoreId();
            System.out.println("print ra storeId " + storeId);
            Join<WarrantyCard, OrderDetail> odJoin = root.join("orderDetail", JoinType.INNER);
            Join<OrderDetail,Orders> oJoin = odJoin.join("order", JoinType.INNER);
            Join<Orders, Store> sJoin = oJoin.join("store", JoinType.INNER);
            return cb.equal(sJoin.get("id"), storeId);
        };
    }
    public static Specification<WarrantyCard> hasOrderDetailId(UUID orderDetailId) {
        return (root, query, cb) -> {
            Join<WarrantyCard, OrderDetail> odJoin = root.join("orderDetail", JoinType.INNER);
            return cb.equal(odJoin.get("id"), orderDetailId);
        };
    }

    public static Specification<WarrantyCard> afterTime(LocalDateTime WarrantyTime) {
        return (root, query, cb) ->  cb.greaterThanOrEqualTo(root.get("startWarrantyTime"), WarrantyTime);
    }

    public static Specification<WarrantyCard> beforeTime(LocalDateTime WarrantyTime) {
        return (root, query, cb) ->  cb.lessThanOrEqualTo(root.get("startWarrantyTime"), WarrantyTime);
    }

    public static Specification<WarrantyCard> hasStatus(GuarantedStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }


}
