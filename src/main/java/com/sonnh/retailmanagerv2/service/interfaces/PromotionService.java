package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;

public interface PromotionService {
    public String createPromotion(PromotionCreateReqDto dto);
}