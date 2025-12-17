package com.sonnh.retailmanagerv2.controller.employee;

import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.ProductByStoreIdResDto;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Product (Employee)")
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeProductController {
    private final OrderInStoreService orderInStoreService;

    @GetMapping({"/getAllProductByStoreId"})
    public ResponseEntity<PageImplResDto<ProductByStoreIdResDto>> getAllProductByStoreId
            (@RequestParam UUID storeId,
             @RequestParam(required = false) String code,
             @RequestParam(required = false) String name,
             @RequestParam(required = false) String brand,
             @RequestParam(required = false) String categoryName,
             @RequestParam(defaultValue = "1") Integer page,
             @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<ProductByStoreIdResDto> result = orderInStoreService.getAllProductByStoreId(storeId, code, name, brand, categoryName, page, size);
        return ResponseEntity.ok(result);
    }}
