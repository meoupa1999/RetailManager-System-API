package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;
@Data
public class SuggestWarehouseReqDto implements Serializable {
    private UUID id;
    private Long quantity;

    public SuggestWarehouseReqDto(UUID id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
