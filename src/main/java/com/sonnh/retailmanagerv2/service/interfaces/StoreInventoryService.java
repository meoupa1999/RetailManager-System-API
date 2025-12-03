package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;

public interface StoreInventoryService {
    PageImplResDto getAllStoreProduct(String code, String name, String brand, Integer page, Integer size);
}