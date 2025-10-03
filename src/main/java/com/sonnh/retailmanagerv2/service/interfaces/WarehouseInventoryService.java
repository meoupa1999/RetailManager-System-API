package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseInventoryCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseInventoryUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryDetailResDtov2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface WarehouseInventoryService {
    PageImplResDto getAllWarehouseInventory(String name, String suplier, Double purchasePriceGreaterThan, Double purchasePriceLessThan, Double salePriceGreaterThan, Double salePriceLessThan, Integer page, Integer size);

    WarehouseInventoryDetailResDtov2 findWarehouseProductById(UUID id);

    UUID createWarehouseProduct(MultipartFile[] files,WarehouseInventoryCreateReqDto dto);

    UUID updateWarehouseProduct(UUID id, WarehouseInventoryUpdateReqDto dto);

    XSSFWorkbook exportProductToExcel(UUID warehouseId);

    void importProductForWarehouse(UUID warehouseId, MultipartFile file);
}
