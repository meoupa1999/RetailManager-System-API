package com.sonnh.retailmanagerv2.data.domain;

import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link Category}
 */
@Value
public class CategoryDto implements Serializable {
    UUID id;
    String name;
    List<StoreInventoryDto> storeInventoryList;

    /**
     * DTO for {@link StoreInventory}
     */
    @Value
    public static class StoreInventoryDto implements Serializable {
        UUID id;
        String productCode;
        String name;
        String brand;
        String description;
    }
}