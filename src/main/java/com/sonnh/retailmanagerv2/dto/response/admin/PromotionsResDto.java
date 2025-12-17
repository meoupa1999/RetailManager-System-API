package com.sonnh.retailmanagerv2.dto.response.admin;

import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Promotion}
 */
@Data
public class PromotionsResDto implements Serializable {
   private UUID id;
   private String name;
   private Double discountPercent;
   private LocalDateTime startDate;
   private LocalDateTime endDate;
   private PromotionStatus status;

    public PromotionsResDto(UUID id,
                            String name,
                            Double discountPercent,
                            LocalDateTime startDate,
                            LocalDateTime endDate,
                            PromotionStatus status) {
        this.id = id;
        this.name = name;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
}