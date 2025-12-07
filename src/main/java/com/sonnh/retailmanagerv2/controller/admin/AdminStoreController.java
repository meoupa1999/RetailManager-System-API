package com.sonnh.retailmanagerv2.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.exception.store_exception.StoreNotFoundException;
import com.sonnh.retailmanagerv2.security.StoreContextDetail;
import com.sonnh.retailmanagerv2.service.interfaces.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@Tag(name = "Store (Admin)")
@RequestMapping("/api/admin")
public class AdminStoreController {
    private static final Logger log = LoggerFactory.getLogger(AdminStoreController.class);
    private StoreService storeService;
    private ObjectMapper objectMapper;

    public AdminStoreController(StoreService storeService, ObjectMapper objectMapper) {
        this.storeService = storeService;
        this.objectMapper = objectMapper;
    }

    @GetMapping({"/getAllStore"})
    public ResponseEntity<PageImplResDto<StoreResDto>> getAllStores(@RequestParam(required = false) String name, @RequestParam(required = false) String phone, @RequestParam(required = false) String mail, @RequestParam(required = false) String address, @RequestParam(required = false) String ward, @RequestParam(required = false) String district, @RequestParam(required = false) String province, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<StoreResDto> result = storeService.getAllStore(name, phone, mail, address, ward, district, province, page, size);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        StoreContextDetail context = (StoreContextDetail) auth.getDetails();
        UUID id = context.getStoreID();
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/getStoreById/{storeId}"})
    public ResponseEntity<StoreDetailResDto> getStoreById(@PathVariable("storeId") UUID id) throws StoreNotFoundException {
        StoreDetailResDto result = this.storeService.findStoreById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(
            value = {"/createStore"},
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<UUID> createStore(@RequestPart("store") String storeJson, @RequestPart("images") MultipartFile images) {
        try {
            StoreCreateReqDto dto = (StoreCreateReqDto)this.objectMapper.readValue(storeJson, StoreCreateReqDto.class);
            return ResponseEntity.ok(this.storeService.createStore(dto));
        } catch (JsonProcessingException ex) {
            log.error("lỗi parse json từ storeJson: {}", storeJson, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
            value = {"/updateStore/{storeId}"},
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<UUID> updateStore(@PathVariable("storeId") UUID id, @RequestPart("store") String storeJson, @RequestPart("images") MultipartFile images) throws StoreNotFoundException {
        try {
            StoreUpdateReqDto dto = (StoreUpdateReqDto)this.objectMapper.readValue(storeJson, StoreUpdateReqDto.class);
            return ResponseEntity.ok(this.storeService.updateStore(id, dto));
        } catch (JsonProcessingException var5) {
            log.error("lỗi parse json từ storeJson: {}", storeJson, var5);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
