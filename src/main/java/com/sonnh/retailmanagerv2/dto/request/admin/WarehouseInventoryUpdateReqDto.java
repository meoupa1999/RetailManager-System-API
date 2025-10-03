package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class WarehouseInventoryUpdateReqDto implements Serializable {
    private String name;
    private String suplier;
    private Double purchasePrice;
    private Double salePrice;
    private Integer warrantyPeriod;

    public WarehouseInventoryUpdateReqDto(String name, String suplier, Double purchasePrice, Double salePrice, Integer warrantyPeriod) {
        this.name = name;
        this.suplier = suplier;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.warrantyPeriod = warrantyPeriod;
    }

}
