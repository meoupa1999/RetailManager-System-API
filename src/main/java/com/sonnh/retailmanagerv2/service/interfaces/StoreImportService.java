package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.admin.ImportStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.SuggestWarehouseReqDto;
import com.sonnh.retailmanagerv2.dto.response.SuggestWarehouseResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ImportStoreReportResDto;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface StoreImportService {
    List<SuggestWarehouseResDto> suggestWarehouse(List<SuggestWarehouseReqDto> dtoList, UUID storeId);

    XSSFWorkbook exportExcelImportStore(ImportStoreReqDto dto);

    void importProductForStore(UUID storeId,MultipartFile file);

    ImportStoreReportResDto reportImportStore(UUID importStoreId, UUID storeId);
}
