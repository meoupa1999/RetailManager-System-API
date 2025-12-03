package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Rank}
 */
@Data
@NoArgsConstructor
public class RankCreateReqDto implements Serializable {
    String name;
    Long spendingThreshold;
    BigDecimal discountPercent;
}