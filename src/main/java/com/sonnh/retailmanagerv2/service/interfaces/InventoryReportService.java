package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;

public interface InventoryReportService {
    PageImplResDto getAllProductOfWarehouseReport(Integer page, Integer size);

    PageImplResDto getAllWarehouseOfProductReport(Integer page, Integer size);
}
