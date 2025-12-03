package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductAndStoreListSpecification {
    public static Specification<StoreInventory> hasPromotionId(UUID promotionId) {
        return (root, query, cb) -> {
            Join<StoreInventory, Store_StoreInventory> ssiJoin = root.join("store_storeInventoryList", JoinType.INNER);
            Join<Store_StoreInventory, Promotion> promotionJoin = ssiJoin.join("promotionStoreStoreInventoryList", JoinType.INNER);
//            Join<Store_StoreInventory, Store> storeJoin = ssiJoin.join("store", JoinType.INNER);
            return cb.equal(promotionJoin.get("id"), promotionId);
//            return cb.isNotEmpty(root.get("store_storeInventoryList"));
        };
    }

    public static Specification<StoreInventory> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
