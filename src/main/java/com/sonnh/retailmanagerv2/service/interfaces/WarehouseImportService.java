package com.sonnh.retailmanagerv2.service.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface WarehouseImportService {
    String importWarehouseProduct(XSSFWorkbook workbook);

}
