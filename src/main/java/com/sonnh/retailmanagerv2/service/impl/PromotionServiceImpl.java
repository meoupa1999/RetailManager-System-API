package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import com.sonnh.retailmanagerv2.data.repository.PromotionRepository;
import com.sonnh.retailmanagerv2.data.repository.Store_StoreInventoryRepository;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.service.interfaces.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final Store_StoreInventoryRepository store_storeInventoryRepository;
    @Override
    @Transactional
    public String createPromotion(PromotionCreateReqDto dto) {

        Promotion promotion = new Promotion();
        promotion.setName(dto.getName());
        promotion.setDiscountPercent(dto.getDiscountPercent());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotionRepository.save(promotion);

        for (PromotionCreateReqDto.ProductDto productDto :dto.getProductDtoList()) {
            UUID productId = productDto.getProductId();
            for (UUID storeId : productDto.getStoreIdList()) {
                Store_StoreInventory store_storeInventory = store_storeInventoryRepository
                                                            .findStoreStoreInventory(storeId,productId);
                store_storeInventory.addPromotion(promotion);
            }
        }
        return "Success";
    }
}