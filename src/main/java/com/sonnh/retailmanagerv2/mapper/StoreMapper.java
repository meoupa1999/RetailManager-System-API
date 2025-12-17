package com.sonnh.retailmanagerv2.mapper;

import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreByProductResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StoreMapper {
    StoreResDto toStoreResDto(Store store);

    StoreDetailResDto toStoreDetailResDto(Store store);

    Store toStoreCreateEntity(StoreCreateReqDto storeCreateReqDto);

    Store toEntity(StoreByProductResDto storeByProductResDto);

    StoreByProductResDto toStoreByProductResDto(Store store);


}
