package com.sonnh.retailmanagerv2.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Promotion}
 */

@NoArgsConstructor
@Data
public class CreatePromotionAllStoreReqDto implements Serializable {
   private String name;
   private Double discountPercent;
   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
   private LocalDateTime startDate;
   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
   private LocalDateTime endDate;

}