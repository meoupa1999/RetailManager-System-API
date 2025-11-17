package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class StoreInventoryByProductSpecification {

    public static Specification<Store> hasProductId(UUID productId) {
        return (root, query, cb) -> {
            // JOIN product -> category
            Join<Store, Store_StoreInventory> ssiJoin = root.join("storeStoreInventoryList", JoinType.INNER);
            Join<Store_StoreInventory, StoreInventory> siJoin = ssiJoin.join("storeInventory", JoinType.INNER);

            return cb.equal(siJoin.get("id"), productId);
        };
    }

    public static Specification<Store> addressContains(String address) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    public static Specification<Store> nameContains(String name) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Store> phoneContains(String phone) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
        };
    }

    public static Specification<Store> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }


}