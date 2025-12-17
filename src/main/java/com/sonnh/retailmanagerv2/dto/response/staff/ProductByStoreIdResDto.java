package com.sonnh.retailmanagerv2.dto.response.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.StoreInventory}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductByStoreIdResDto implements Serializable {
   private UUID id;
   private String productCode;
   private String name;
   private String brand;
   private CategoryDto category;

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Category}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto implements Serializable {
       private UUID id;
       private String name;
    }
}