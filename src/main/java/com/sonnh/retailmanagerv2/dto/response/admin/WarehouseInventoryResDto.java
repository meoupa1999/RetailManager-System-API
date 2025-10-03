package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;
@Data
public class WarehouseInventoryResDto implements Serializable {
    private UUID id;
    private String name;
    private String productCode;
    private String suplier;
    private Double purchasePrice;
    private Double salePrice;
    private Integer warrantyPeriod;

    public WarehouseInventoryResDto(UUID id, String productCode, String name, String suplier, Double purchasePrice, Double salePrice, Integer warrantyPeriod) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.suplier = suplier;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.warrantyPeriod = warrantyPeriod;
    }
}
