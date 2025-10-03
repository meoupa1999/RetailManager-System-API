package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.WarehouseInventory}
 */
@Data
public class WarehouseContainProductResDto implements Serializable {
    UUID id;
    String productCode;
    String name;
    List<Warehouse_WarehouseInventoryDto> warehouse_warehouseInventoryList;

    public WarehouseContainProductResDto(UUID id, String productCode, String name, List<Warehouse_WarehouseInventoryDto> warehouse_warehouseInventoryList) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.warehouse_warehouseInventoryList = warehouse_warehouseInventoryList;
    }

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Warehouse_WarehouseInventory}
     */
    @Data
    public static class Warehouse_WarehouseInventoryDto implements Serializable {
        UUID id;
        long quantity;
        WarehouseDto warehouse;

        public Warehouse_WarehouseInventoryDto(UUID id, long quantity, WarehouseDto warehouse) {
            this.id = id;
            this.quantity = quantity;
            this.warehouse = warehouse;
        }

        /**
         * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Warehouse}
         */
        @Data
        public static class WarehouseDto implements Serializable {
            UUID id;
            String name;

            public WarehouseDto(UUID id, String name) {
                this.id = id;
                this.name = name;
            }
        }
    }
}