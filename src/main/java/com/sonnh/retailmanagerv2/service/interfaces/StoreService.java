package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreDetailResDto;

import java.util.UUID;

public interface StoreService {
    PageImplResDto getAllStore(String name, String phone, String mail, String address, String ward, String district, String province, Integer page, Integer size);

    StoreDetailResDto findStoreById(UUID id);

    UUID createStore(StoreCreateReqDto dto);

    UUID updateStore(UUID storeId, StoreUpdateReqDto dto);
}
