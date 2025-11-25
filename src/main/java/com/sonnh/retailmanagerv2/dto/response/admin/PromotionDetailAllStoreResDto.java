package com.sonnh.retailmanagerv2.dto.response.admin;

import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionType;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Promotion}
 */
@Data
public class PromotionDetailAllStoreResDto implements Serializable,PromotionDetailResDto {
    private UUID id;
    private String name;
    private Double discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PromotionStatus status;
    private PromotionType type;
    private Set<StoreInventoryDto> storeInventoryList;

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.StoreInventory}
     */
    @Data
    public static class StoreInventoryDto implements Serializable {
        private UUID id;
        private String productCode;
        private String name;
        private String brand;
    }
}