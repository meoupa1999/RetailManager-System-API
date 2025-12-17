package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductByStoreIdSpecification {

    public static Specification<StoreInventory> hasStoreId(UUID storeId) {
        return (root, query, cb) -> {
            Join<StoreInventory, Store_StoreInventory> ssiJoin = root.join("store_storeInventoryList", JoinType.INNER);
            Join<Store_StoreInventory, Store> sJoin = ssiJoin.join("store", JoinType.INNER);
            return cb.equal(sJoin.get("id"), storeId);
        };
    }

    public static Specification<StoreInventory> productCode(String productCode) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("productCode")), "%" + productCode.toLowerCase() + "%");
    }

    public static Specification<StoreInventory> nameContains(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<StoreInventory> brandContains(String brand) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    }

    public static Specification<StoreInventory> categoryNameContains(String categoryName) {
        return (root, query, cb) -> {
            Join<StoreInventory, Category> cJoin = root.join("category", JoinType.INNER);
            return cb.like(cb.lower(cJoin.get("name")), "%" + categoryName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<StoreInventory> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
