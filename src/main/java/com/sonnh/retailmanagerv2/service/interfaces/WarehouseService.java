package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseDetailResDto;

import java.util.UUID;

public interface WarehouseService {
    PageImplResDto getAllWarehouse(String name, String address, String phone, String email, String description, Integer page, Integer size);

    WarehouseDetailResDto findWarehouseById(UUID id);

    UUID createWarehouse(WarehouseCreateReqDto dto);

    UUID updateWarehouse(UUID id, WarehouseUpdateReqDto dto);
}
