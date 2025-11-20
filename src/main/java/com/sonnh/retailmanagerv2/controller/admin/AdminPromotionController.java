package com.sonnh.retailmanagerv2.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonnh.retailmanagerv2.dto.request.admin.CreatePromotionAllStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreByProductResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.service.interfaces.PromotionService;
import com.sonnh.retailmanagerv2.service.interfaces.StoreService;
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
@Tag(name = "Promotion (Admin)")
@RequestMapping("/api/admin")
public class AdminPromotionController {
    private static final Logger log = LoggerFactory.getLogger(AdminStoreController.class);
    private PromotionService promotionService;
    private ObjectMapper objectMapper;

    public AdminPromotionController(PromotionService promotionService, ObjectMapper objectMapper) {
        this.promotionService = promotionService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(
            value = {"/createPromotion"},
            consumes = {"multipart/form-data"}
    )
    public String createPromotion(@RequestPart("promotion") String promotionJson, @RequestPart("images") MultipartFile images) {
        try {
            PromotionCreateReqDto dto = (PromotionCreateReqDto) this.objectMapper.readValue(promotionJson, PromotionCreateReqDto.class);
//            return ResponseEntity.ok(this.promotionService.createPromotion(dto));
            return this.promotionService.createPromotion(dto);
        } catch (JsonProcessingException ex) {
            log.error("lỗi parse json từ promotionJson: {}", promotionJson, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/getStoresByProductId"})
    public ResponseEntity<PageImplResDto<StoreByProductResDto>> getStoresByProductId(@RequestParam UUID productId,
                                                                                     @RequestParam(required = false) String address,
                                                                                     @RequestParam(required = false) String name,
                                                                                     @RequestParam(required = false) String phone,
                                                                                     @RequestParam(defaultValue = "1") Integer page,
                                                                                     @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<StoreByProductResDto> result = promotionService.getStoreByProductId(productId, address, name, phone, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/createPromotionAllStore")
    public String createPromotionAllStore(@RequestParam UUID productId,
                                          @RequestBody CreatePromotionAllStoreReqDto promotionDto) {
        String result = promotionService.createPromotionAllStore(productId, promotionDto);
        return result;
    }
}
