package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.WarehouseInventory}
 */
@Data
public class WarehouseInventoryDetailResDtov2 implements Serializable {
    UUID id;
    String productCode;
    String name;
    String description;
    String suplier;
    String unitOfMeasure;
    Double purchasePrice;
    Double salePrice;
    Integer warrantyPeriod;
    AuditDto audit;
    List<ProductImageDto> productImageList;

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.embedded.Audit}
     */
    @Data
    public static class AuditDto implements Serializable {
        String createdBy;
        LocalDateTime createdAt;
        String updatedBy;
        LocalDateTime updatedAt;
        Boolean isActive;
    }

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.ProductImage}
     */
    @Data
    public static class ProductImageDto implements Serializable {
        String url;
    }
}