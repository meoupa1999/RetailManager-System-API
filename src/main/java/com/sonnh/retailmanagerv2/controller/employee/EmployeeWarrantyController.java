package com.sonnh.retailmanagerv2.controller.employee;

import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import com.sonnh.retailmanagerv2.dto.request.staff.CreateWarrantyCardReqDto;
import com.sonnh.retailmanagerv2.dto.request.staff.UpdateWarrantyCardReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.CheckWarrantyDto;
import com.sonnh.retailmanagerv2.dto.response.staff.ProductByStoreIdResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.WarrantyCardDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.WarrantyCardResDto;
import com.sonnh.retailmanagerv2.service.interfaces.WarrantyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Tag(name = "Warranty (Employee)")
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeWarrantyController {
    private final WarrantyService warrantyService;

    @GetMapping({"/checkWarranty/{orderdetailId}"})
    public ResponseEntity<CheckWarrantyDto> checkWarranty
            (@PathVariable UUID orderdetailId) {
        CheckWarrantyDto result = warrantyService.checkWarrantyByOdId(orderdetailId);
        return ResponseEntity.ok(result);
    }

    @PostMapping({"/createWarrantyCard"})
    public String createWarrantyCard(@RequestBody CreateWarrantyCardReqDto dto) {
        System.out.println();
        return warrantyService.createWarranty(dto);
    }

    @PutMapping({"/updateWarranty"})
    public String updateWarranty(@RequestParam UUID warrantyCardId, @RequestBody UpdateWarrantyCardReqDto dto) {
        return warrantyService.updateWarranty(warrantyCardId, dto);
    }

    @GetMapping({"/getAllWarrantyCard"})
    public ResponseEntity<PageImplResDto<?>> getAllWarrantyCard
            (@RequestParam (required = false) UUID orderdetailId,
             @RequestParam (required = false) LocalDateTime afterTime,
             @RequestParam (required = false) LocalDateTime beforeTime,
             @RequestParam (required = false) GuarantedStatus status,
             @RequestParam (defaultValue = "1") Integer page,
             @RequestParam (defaultValue = "100") Integer size) {
        PageImplResDto<WarrantyCardResDto> result = warrantyService.getAllWarrantyCard(orderdetailId, afterTime, beforeTime, status, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/getWarrantyCardById/{warrantyId}"})
    public ResponseEntity<WarrantyCardDetailResDto> getWarrantyCardById
            (@PathVariable UUID warrantyId) {
        WarrantyCardDetailResDto result = warrantyService.findWarrantyCardById(warrantyId);
        return ResponseEntity.ok(result);
    }

}

