package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.dto.request.admin.CreatePromotionAllStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductAndStoreListResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.PromotionDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreByProductResDto;

import java.util.List;
import java.util.UUID;

public interface PromotionService {
    public String createPromotion(PromotionCreateReqDto dto);

    public PageImplResDto getStoreByProductId(UUID productId, String address, String name, String phone, Integer page, Integer size);

    public String createPromotionAllStore(CreatePromotionAllStoreReqDto dto);

    public PageImplResDto getAllPromotion(String name, PromotionStatus status, Integer page, Integer size);

    public PageImplResDto getProductsByPromotionId(UUID promotionId, String code, String name, String brand, Integer page, Integer size);

//    public PageImplResDto getProductAndStoreByPromotionId(UUID promotionId, Integer page, Integer size);

    public ProductAndStoreListResDto getProductAndStoreByPromotionId(UUID promotionId);

    public String updatePromotion(UUID promotionId, PromotionUpdateReqDto dto);

    public PromotionDetailResDto getPromotionDetail(UUID promotionId);

    public String deletePromotion(UUID promotionId);

}
