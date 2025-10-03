package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Warehouse}
 */
@Data
public class ProductOfWarehouseResDto implements Serializable {
    UUID id;
    String name;
    List<Warehouse_WarehouseInventoryDto> warehouseWarehouseInventoryList;

    public ProductOfWarehouseResDto(UUID id, String name, List<Warehouse_WarehouseInventoryDto> warehouseWarehouseInventoryList) {
        this.id = id;
        this.name = name;
        this.warehouseWarehouseInventoryList = warehouseWarehouseInventoryList;
    }

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Warehouse_WarehouseInventory}
     */
    @Data
    public static class Warehouse_WarehouseInventoryDto implements Serializable {
        UUID id;
        long quantity;
        WarehouseInventoryDto warehouseInventory;

        public Warehouse_WarehouseInventoryDto(UUID id, long quantity, WarehouseInventoryDto warehouseInventory) {
            this.id = id;
            this.quantity = quantity;
            this.warehouseInventory = warehouseInventory;
        }

        /**
         * DTO for {@link com.sonnh.retailmanagerv2.data.domain.WarehouseInventory}
         */
        @Data
        public static class WarehouseInventoryDto implements Serializable {
            UUID id;
            String name;

            public WarehouseInventoryDto(UUID id, String name) {
                this.id = id;
                this.name = name;
            }
        }
    }
}