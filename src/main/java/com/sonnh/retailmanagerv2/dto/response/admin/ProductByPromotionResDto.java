package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.StoreInventory}
 */
@Data
public class ProductByPromotionResDto implements Serializable {
   private UUID id;
   private String name;
   private String brand;

    public ProductByPromotionResDto(UUID id, String name, String brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }
}