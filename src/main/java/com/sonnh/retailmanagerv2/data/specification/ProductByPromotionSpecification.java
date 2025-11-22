package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductByPromotionSpecification {

    public static Specification<StoreInventory> hasPromotionId(UUID promotionId) {
        return (root, query, cb) -> {
            Join<StoreInventory, Promotion> promotionJoin = root.join("promotionList", JoinType.INNER);
            return cb.equal(promotionJoin.get("id"), promotionId);
        };
    }

    public static Specification<StoreInventory> productCodeContains(String productCode) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("productCode")), "%" + productCode.toLowerCase() + "%");
        };
    }

    public static Specification<StoreInventory> nameContains(String name) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<StoreInventory> brandContains(String brand) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
        };
    }

    public static Specification<StoreInventory> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
