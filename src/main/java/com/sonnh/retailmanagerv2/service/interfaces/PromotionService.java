package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreByProductResDto;

import java.util.UUID;

public interface PromotionService {
    public String createPromotion(PromotionCreateReqDto dto);

    public PageImplResDto getStoreByProductId(UUID productId, String address, String name, String phone,Integer page, Integer size);
}
