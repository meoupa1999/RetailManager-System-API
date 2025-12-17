package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Rank}
 */
@Data
public class RankResDto implements Serializable {
    UUID id;
    String name;
    Long spendingThreshold;
    BigDecimal discountPercent;

    public RankResDto(UUID id, String name, Long spendingThreshold, BigDecimal discountPercent) {
        this.id = id;
        this.name = name;
        this.spendingThreshold = spendingThreshold;
        this.discountPercent = discountPercent;
    }
}