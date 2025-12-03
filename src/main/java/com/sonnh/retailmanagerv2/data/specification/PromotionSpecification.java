package com.sonnh.retailmanagerv2.data.specification;

import com.sonnh.retailmanagerv2.data.domain.Promotion;

import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PromotionSpecification {

    public static Specification<Promotion> nameContains(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }



    public static Specification<Promotion> isActive() {
        return (root, query, cb) -> {
            return cb.isTrue(root.get("audit").get("isActive"));
        };
    }
}
