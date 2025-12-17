package com.sonnh.retailmanagerv2.controller.employee;

import com.azure.core.annotation.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.customer.CreateDraftOrderReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.dto.response.customer.DraftOrderResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.ProductByStoreIdResDto;
import com.sonnh.retailmanagerv2.security.StoreContextDetail;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Order (Employee)")
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeOrderController {
    private final OrderInStoreService orderInStoreService;

    @PostMapping(value = {"/createDraftOrder"})
    public ResponseEntity<DraftOrderResDto> createDraftOrder(@RequestBody CreateDraftOrderReqDto dto) {
        DraftOrderResDto result = orderInStoreService.createDraftOrder(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/acceptDraftOrder")
    public String acceptDraftOrder(@RequestParam UUID draftId) {
        orderInStoreService.acceptDraftOrder(draftId);
        return "Success";
    }

    @DeleteMapping(value = "/cancelDraftOrder")
    public String cancelDraftOrder(@RequestParam UUID draftId) {
        orderInStoreService.cancelDraftOrder(draftId);
        return "Success";
    }

    @GetMapping(value = "/getAllDraftOrder")
    public ResponseEntity<List<DraftOrderResDto>> getAllDraftOrder() {
        return ResponseEntity.ok(orderInStoreService.getAllDraftOrder());
    }

}
