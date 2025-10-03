package com.sonnh.retailmanagerv2.controller.admin;

import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductOfWarehouseResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseContainProductResDto;
import com.sonnh.retailmanagerv2.service.interfaces.InventoryReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Report (Admin)")
@RequestMapping("/api/admin")
public class AdminReportController {
    private InventoryReportService inventoryReportService;

    public AdminReportController(InventoryReportService inventoryReportService) {
        this.inventoryReportService = inventoryReportService;
    }

    @GetMapping({"/productOfWarehouseReport"})
    public ResponseEntity<PageImplResDto<ProductOfWarehouseResDto>> getProductOfWarehouseReport(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<ProductOfWarehouseResDto> result = inventoryReportService.getAllProductOfWarehouseReport(page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/warehouseOfProductReport"})
    public ResponseEntity<PageImplResDto<WarehouseContainProductResDto>> getWarehouseOfProductReport(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<WarehouseContainProductResDto> result = inventoryReportService.getAllWarehouseOfProductReport(page, size);
        return ResponseEntity.ok(result);
    }
}
