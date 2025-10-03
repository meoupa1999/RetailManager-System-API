package com.sonnh.retailmanagerv2.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseInventoryCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseInventoryUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryDetailResDtov2;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryResDto;
import com.sonnh.retailmanagerv2.exception.warehouseinventory_exception.WarehouseInventoryNotFoundException;
import com.sonnh.retailmanagerv2.service.interfaces.WarehouseInventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@Tag(name = "WarehouseInventory (Admin)")
@RequestMapping({"/api/admin"})
public class AdminWarehouseInventoryController {
    private static final Logger log = LoggerFactory.getLogger(AdminWarehouseInventoryController.class);
    private WarehouseInventoryService warehouseInventoryService;
    private ObjectMapper objectMapper;

    public AdminWarehouseInventoryController(WarehouseInventoryService warehouseInventoryService, ObjectMapper objectMapper) {
        this.warehouseInventoryService = warehouseInventoryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping({"/findAllWarehouseProduct"})
    public ResponseEntity<PageImplResDto<WarehouseInventoryResDto>> findAllWarehouseProduct(@RequestParam(required = false) String name, @RequestParam(required = false) String suplier, @RequestParam(required = false) Double purchasePriceGreaterThan, @RequestParam(required = false) Double purchasePriceLessThan, @RequestParam(required = false) Double salePriceGreaterThan, @RequestParam(required = false) Double salePriceLessThan, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<WarehouseInventoryResDto> result = this.warehouseInventoryService.getAllWarehouseInventory(name, suplier, purchasePriceGreaterThan, purchasePriceLessThan, salePriceGreaterThan, salePriceLessThan, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/getWarehouseProductById/{warehouseProductId}"})
    public ResponseEntity<WarehouseInventoryDetailResDtov2> getWarehouseProductyId(@PathVariable("warehouseProductId") UUID id) throws WarehouseInventoryNotFoundException {
        WarehouseInventoryDetailResDtov2 result = this.warehouseInventoryService.findWarehouseProductById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(
            value = {"/createWarehouseProduct"},
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<UUID> createWarehouseProduct(
            @RequestPart("warehouseProduct") String warehouseProductJson, @RequestPart("images") MultipartFile[] images) {
        try {
            WarehouseInventoryCreateReqDto dto = (WarehouseInventoryCreateReqDto) this.objectMapper.readValue(warehouseProductJson, WarehouseInventoryCreateReqDto.class);
            return ResponseEntity.ok(this.warehouseInventoryService.createWarehouseProduct(images,dto));
        } catch (JsonProcessingException ex) {
            log.error("lỗi parse json từ warehouseJson: {}", warehouseProductJson, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
            value = {"/updateWarehouseProduct/{warehouseProductId}"},
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<UUID> updateWarehouseProduct(@PathVariable("warehouseProductId") UUID id, @RequestPart("warehouseProduct") String warehouseProductJson, @RequestPart("images") MultipartFile images) throws WarehouseInventoryNotFoundException {
        try {
            WarehouseInventoryUpdateReqDto dto = (WarehouseInventoryUpdateReqDto) this.objectMapper.readValue(warehouseProductJson, WarehouseInventoryUpdateReqDto.class);
            return ResponseEntity.ok(this.warehouseInventoryService.updateWarehouseProduct(id, dto));
        } catch (JsonProcessingException ex) {
            log.error("lỗi parse json từ storeJson: {}", warehouseProductJson, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/exportProductFile/{warehouseId}"})
    public ResponseEntity<byte[]> exportExcel(@PathVariable UUID warehouseId) {
        try {
            XSSFWorkbook workbook = this.warehouseInventoryService.exportProductToExcel(warehouseId);
            ResponseEntity result;
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    workbook.write(out);
                    byte[] bytes = out.toByteArray();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                    headers.setContentDisposition(ContentDisposition.attachment().filename("product-inventory.xlsx").build());
                    result = new ResponseEntity(bytes, headers, HttpStatus.OK);
                } catch (Throwable ex1) {
                    try {
                        out.close();
                    } catch (Throwable ex2) {
                        ex1.addSuppressed(ex2);
                    }
                    throw ex1;
                }

                out.close();
            } catch (Throwable ex3) {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (Throwable ex4) {
                        ex3.addSuppressed(ex4);
                    }
                }
                throw ex3;
            }
            if (workbook != null) {
                workbook.close();
            }
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(
            value = {"/importProductForWarehouse"},consumes = {"multipart/form-data"})
    public ResponseEntity<String> importProductForWarehouse(@RequestParam("warehouseId") UUID warehouseId, @RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        } else {
            String fileName = file.getOriginalFilename();
            if (fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
                try {
                    this.warehouseInventoryService.importProductForWarehouse(warehouseId, file);
                    return ResponseEntity.ok("Import thành công");
                } catch (Exception var5) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Import thất bại: " + var5.getMessage());
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid file type, must be Excel");
            }
        }
    }
}
