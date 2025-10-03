package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.WarehouseInventory;
import org.springframework.data.jpa.domain.Specification;

public class WarehouseInventorySpecification {
    public static Specification<WarehouseInventory> nameContains(String name) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<WarehouseInventory> suplierContains(String suplier) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("suplier")), "%" + suplier.toLowerCase() + "%");
        };
    }

    public static Specification<WarehouseInventory> purchasePriceGreaterThan(Double purchasePriceGreaterThan) {
        return (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.get("purchasePrice"), purchasePriceGreaterThan);
        };
    }

    public static Specification<WarehouseInventory> purchasePriceLessThan(Double purchasePriceLessThan) {
        return (root, query, cb) -> {
            return cb.lessThanOrEqualTo(root.get("purchasePrice"), purchasePriceLessThan);
        };
    }

    public static Specification<WarehouseInventory> salePriceGreaterThan(Double salePriceGreaterThan) {
        return (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.get("salePrice"), salePriceGreaterThan);
        };
    }

    public static Specification<WarehouseInventory> salePriceLessThan(Double salePriceLessThan) {
        return (root, query, cb) -> {
            return cb.lessThanOrEqualTo(root.get("salePrice"), salePriceLessThan);
        };
    }

    public static Specification<WarehouseInventory> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
