package com.sonnh.retailmanagerv2.controller.employee;

import com.azure.core.annotation.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.customer.CreateDraftOrderReqDto;
import com.sonnh.retailmanagerv2.dto.response.customer.DraftOrderResDto;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
        System.out.println("run controller");
        orderInStoreService.acceptDraftOrder(draftId);
        return "Success";
    }

    @DeleteMapping(value = "/cancelDraftOrder")
    public String cancelDraftOrder(@RequestParam UUID draftId) {
        System.out.println("draft Id: " + draftId);
        orderInStoreService.cancelDraftOrder(draftId);
        return "Success";
    }

}
