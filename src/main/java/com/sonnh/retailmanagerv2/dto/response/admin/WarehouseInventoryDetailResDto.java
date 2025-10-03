package com.sonnh.retailmanagerv2.dto.response.admin;

import com.sonnh.retailmanagerv2.data.domain.ProductImage;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
public class WarehouseInventoryDetailResDto implements Serializable {
    private UUID id;
    private  String productCode;
    private  String name;
    private  String suplier;
    private Double purchasePrice;
    private Double salePrice;
    private Integer warrantyPeriod;
    private AuditDto audit;
    private List<ProductImageDto> productImageListDto;


    public WarehouseInventoryDetailResDto(UUID id, String productCode, String name, String suplier, Double purchasePrice, Double salePrice, Integer warrantyPeriod, AuditDto audit) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.suplier = suplier;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.warrantyPeriod = warrantyPeriod;
        this.audit = audit;
    }
    @Data
    public static class ProductImageDto implements Serializable{
        private String url;
    }

    @Data
    public static class AuditDto implements Serializable {
        String createdBy;
        LocalDateTime createdAt;
        String updatedBy;
        LocalDateTime updatedAt;
        Boolean isActive;
    }

}
