package com.sonnh.retailmanagerv2.mapper;

import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.WarehouseInventory;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseInventoryCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.admin.*;
import com.sonnh.retailmanagerv2.dto.response.staff.ProductByStoreIdResDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
//    ProductsOfStoreDto toProductOfStoreDto(Store store);

    WarehouseInventoryResDto toWarehouseInventoryResDto(WarehouseInventory warehouseInventory);

//    WarehouseInventoryDetailResDto toWarehouseInventoryDetailDto(WarehouseInventory warehouseInventory);

    //    @Mapping(target = "audit", source = "audit")
    WarehouseInventory toWarehouseInventoryCreateEntity(WarehouseInventoryCreateReqDto warehouseInventoryCreateReqDto);
//
//    //    WarehouseInventoryDetailResDto.AuditDto toAuditDto(Audit audit);
//    WarehouseInventoryDetailResDto.AuditDto map(Audit audit);

    WarehouseInventoryExcelResDto toWarehouseInventoryExcelResDto(WarehouseInventory warehouseInventory);


    WarehouseContainProductResDto toWarehouseContainProductResDto(WarehouseInventory warehouseInventory);


    WarehouseInventoryDetailResDtov2 toWarehouseInventoryDetailDtov2(WarehouseInventory warehouseInventory);

    ProductByStoreIdResDto toProductByStoreIdResDto(StoreInventory storeInventory);


}
