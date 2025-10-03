package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.Warehouse;
import org.springframework.data.jpa.domain.Specification;

public class ProductOfWarehouseReportSpecification {
    public static Specification<Warehouse> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
