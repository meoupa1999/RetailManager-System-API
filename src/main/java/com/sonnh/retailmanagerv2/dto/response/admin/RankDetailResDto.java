package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Rank}
 */
@Data
public class RankDetailResDto implements Serializable {
    UUID id;
    String name;
    Long spendingThreshold;
    BigDecimal discountPercent;
    AuditDto audit;

    public RankDetailResDto(UUID id, String name, Long spendingThreshold, BigDecimal discountPercent, AuditDto audit) {
        this.id = id;
        this.name = name;
        this.spendingThreshold = spendingThreshold;
        this.discountPercent = discountPercent;
        this.audit = audit;
    }

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.embedded.Audit}
     */
    @Value
    public static class AuditDto implements Serializable {
        String createdBy;
        LocalDateTime createdAt;
        String updatedBy;
        LocalDateTime updatedAt;
        Boolean isActive;

        public AuditDto(String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt, Boolean isActive) {
            this.createdBy = createdBy;
            this.createdAt = createdAt;
            this.updatedBy = updatedBy;
            this.updatedAt = updatedAt;
            this.isActive = isActive;
        }
    }
}