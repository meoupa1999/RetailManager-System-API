package com.sonnh.retailmanagerv2.mapper;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.dto.request.admin.CreatePromotionAllStoreReqDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductAndStoreListResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductByPromotionResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.PromotionsResDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PromotionMapper {
    Promotion toPromotionEntity(CreatePromotionAllStoreReqDto createPromotionAllStoreReqDto);


//    Promotion toEntity(PromotionsResDto promotionsResDto);

    PromotionsResDto toPromotionsResDto(Promotion promotion);


//    StoreInventory toEntity(ProductByPromotionResDto productByPromotionResDto);

    ProductByPromotionResDto toProductByPromotionResDto(StoreInventory storeInventory);

//    StoreInventory toEntity(ProductAndStoreListResDto productAndStoreListResDto);

//    ProductAndStoreListResDto toProductAndStoreListResDto(StoreInventory storeInventory);

}