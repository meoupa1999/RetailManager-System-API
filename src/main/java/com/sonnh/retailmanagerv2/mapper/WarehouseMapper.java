package com.sonnh.retailmanagerv2.mapper;

import com.sonnh.retailmanagerv2.data.domain.Warehouse;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductOfWarehouseResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseResDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    WarehouseResDto toWarehouseResDto(Warehouse warehouse);

    WarehouseDetailResDto toWarehouseDetailResDto(Warehouse warehouse);

    Warehouse toWarehouseEntity(WarehouseCreateReqDto warehouseCreateReqDto);



    ProductOfWarehouseResDto toProductOfWarehouseResDto(Warehouse warehouse);


}
