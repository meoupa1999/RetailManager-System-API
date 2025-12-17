package com.sonnh.retailmanagerv2.dto.request.staff;

import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.WarrantyCard}
 */
@Value
public class UpdateWarrantyCardReqDto implements Serializable {
    String description;
    LocalDateTime completeWarrantyTime;
    GuarantedStatus status;
}