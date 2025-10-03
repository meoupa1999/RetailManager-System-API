package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.Store;
import org.springframework.data.jpa.domain.Specification;

public class StoreSpecification {
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

    public static Specification<Store> addressContains(String address) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    public static Specification<Store> descriptionContains(String description) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<Store> provinceContains(String province) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("province")), "%" + province.toLowerCase() + "%");
        };
    }

    public static Specification<Store> districtContains(String district) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("district")), "%" + district.toLowerCase() + "%");
        };
    }

    public static Specification<Store> wardContains(String ward) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("ward")), "%" + ward.toLowerCase() + "%");
        };
    }

    public static Specification<Store> mailContains(String mail) {
        return (root, query, cb) -> {
            return cb.like(cb.lower(root.get("mail")), "%" + mail.toLowerCase() + "%");
        };
    }

    public static Specification<Store> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
