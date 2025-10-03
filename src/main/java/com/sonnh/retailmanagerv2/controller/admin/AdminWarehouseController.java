package com.sonnh.retailmanagerv2.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseResDto;
import com.sonnh.retailmanagerv2.exception.warehouse_exception.WarehouseNotFoundException;
import com.sonnh.retailmanagerv2.service.interfaces.WarehouseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@Tag(name = "Warehouse (Admin)")
@RequestMapping({"/api/admin"})
public class AdminWarehouseController {
    private static final Logger log = LoggerFactory.getLogger(AdminWarehouseController.class);
    private WarehouseService warehouseService;
    private ObjectMapper objectMapper;

    public AdminWarehouseController(WarehouseService warehouseService, ObjectMapper objectMapper) {
        this.warehouseService = warehouseService;
        this.objectMapper = objectMapper;
    }

    @GetMapping({"/getAllWarehouse"})
    public ResponseEntity<PageImplResDto<WarehouseResDto>> getAllWarehouse(@RequestParam(required = false) String name, @RequestParam(required = false) String address, @RequestParam(required = false) String phone, @RequestParam(required = false) String email, @RequestParam(required = false) String description, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<WarehouseResDto> result = this.warehouseService.getAllWarehouse(name, address, phone, email, description, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/getWarehouseById/{warehouseId}"})
    public ResponseEntity<WarehouseDetailResDto> getWarehouseById(@PathVariable("warehouseId") UUID id) throws WarehouseNotFoundException {
        WarehouseDetailResDto result = this.warehouseService.findWarehouseById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(
            value = {"/createWarehouse"},
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<UUID> createWarehouse(@RequestPart("warehouse") String warehouseJson, @RequestPart("images") MultipartFile images) {
        try {
            WarehouseCreateReqDto dto = (WarehouseCreateReqDto)this.objectMapper.readValue(warehouseJson, WarehouseCreateReqDto.class);
            return ResponseEntity.ok(this.warehouseService.createWarehouse(dto));
        } catch (JsonProcessingException ex) {
            log.error("lỗi parse json từ warehouseJson: {}", warehouseJson, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
            value = {"/updateWarehouse/{warehouseId}"},
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<UUID> updateWarehouse(@PathVariable("warehouseId") UUID id, @RequestPart("warehouse") String warehouseJson, @RequestPart("images") MultipartFile images) throws WarehouseNotFoundException {
        try {
            WarehouseUpdateReqDto dto = (WarehouseUpdateReqDto)this.objectMapper.readValue(warehouseJson, WarehouseUpdateReqDto.class);
            return ResponseEntity.ok(this.warehouseService.updateWarehouse(id, dto));
        } catch (JsonProcessingException ex) {
            log.error("lỗi parse json từ storeJson: {}", warehouseJson, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
