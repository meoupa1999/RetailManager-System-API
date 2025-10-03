package com.sonnh.retailmanagerv2.mapper;

import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StoreMapper {
    StoreResDto toStoreResDto(Store store);

    StoreDetailResDto toStoreDetailResDto(Store store);

    Store toStoreCreateEntity(StoreCreateReqDto storeCreateReqDto);

}
