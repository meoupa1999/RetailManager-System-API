package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class WarehouseInventoryExcelResDto implements Serializable {
    private UUID id;
    private String productCode;
    private String name;
    private String unitOfMeasure;
    private List<Warehouse_WarehouseInventoryDto> warehouse_warehouseInventoryList;

    public WarehouseInventoryExcelResDto(UUID id, String productCode, String name, String unitOfMeasure, List<Warehouse_WarehouseInventoryDto> warehouse_warehouseInventoryList) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.warehouse_warehouseInventoryList = warehouse_warehouseInventoryList;
    }

    @Data
    public static class Warehouse_WarehouseInventoryDto implements Serializable{
        private long quantity;

        public Warehouse_WarehouseInventoryDto(long quantity) {
            this.quantity = quantity;
        }
    }
}
