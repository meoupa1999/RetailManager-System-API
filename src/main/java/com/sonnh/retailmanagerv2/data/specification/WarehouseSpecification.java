package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.Warehouse;
import org.springframework.data.jpa.domain.Specification;

public class WarehouseSpecification {
    public static Specification<Warehouse> nameContains(String name) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Warehouse> addressContains(String address) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    public static Specification<Warehouse> phoneContains(String phone) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
        };
    }

    public static Specification<Warehouse> emailContains(String email) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<Warehouse> descriptionContains(String description) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<Warehouse> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
