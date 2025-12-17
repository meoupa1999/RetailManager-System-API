package com.sonnh.retailmanagerv2.controller.admin;

import com.azure.core.annotation.Put;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.dto.request.admin.CreatePromotionAllStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.*;
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

import java.util.List;
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
    public String createPromotionAllStore(@RequestBody CreatePromotionAllStoreReqDto promotionDto) {
        String result = promotionService.createPromotionAllStore(promotionDto);
        return result;
    }

    @GetMapping({"/getAllPromotion"})
    public ResponseEntity<PageImplResDto<PromotionsResDto>> getAllPromotion(@RequestParam(required = false) String name, @RequestParam(required = false) PromotionStatus status, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<PromotionsResDto> result = promotionService.getAllPromotion(name, status, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/getAllProductByPromotionId"})
    public ResponseEntity<PageImplResDto<ProductByPromotionResDto>> getAllProductByPromotionId(@RequestParam UUID promotionId,
                                                                                               @RequestParam(required = false) String code,
                                                                                               @RequestParam(required = false) String name,
                                                                                               @RequestParam(required = false) String brand,
                                                                                               @RequestParam(defaultValue = "1") Integer page,
                                                                                               @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<ProductByPromotionResDto> result = promotionService.getProductsByPromotionId(promotionId, code,name,brand, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/getProductAndStoreByPromotionId"})
    public ResponseEntity<ProductAndStoreListResDto> getProductAndStoreByPromotionId(@RequestParam UUID promotionId) {
        ProductAndStoreListResDto result = promotionService.getProductAndStoreByPromotionId(promotionId);
        return ResponseEntity.ok(result);
    }

    @PutMapping(
            value = "/updatePromotion")
    public String updatePromotion(@RequestParam UUID promotionId, @RequestBody PromotionUpdateReqDto dto) {
        String result = promotionService.updatePromotion(promotionId,dto);
        return result;
    }

    @GetMapping({"/getPromotionDetail"})
    public ResponseEntity<PromotionDetailResDto> getPromotionDetail(@RequestParam UUID promotionId) {
        PromotionDetailResDto result = promotionService.getPromotionDetail(promotionId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping({"/deletePromotion"})
    public String deletePromotion(@RequestParam UUID promotionId) {
        String result = promotionService.deletePromotion(promotionId);
        return result;
    }
}
