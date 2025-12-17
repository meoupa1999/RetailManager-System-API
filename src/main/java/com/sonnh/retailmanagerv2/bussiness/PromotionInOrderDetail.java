package com.sonnh.retailmanagerv2.bussiness;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionInOrderDetail {
    private UUID promotionId;
    private Long discountAmmount;
}
