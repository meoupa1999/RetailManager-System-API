package com.sonnh.retailmanagerv2.dto.request.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.WarrantyCard}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWarrantyCardReqDto implements Serializable {
    private UUID orderDetailId;
    private UUID warrantyId;
    private String description;
    private LocalDateTime startWarrantyTime;
    private LocalDateTime completeWarrantyTime;
}