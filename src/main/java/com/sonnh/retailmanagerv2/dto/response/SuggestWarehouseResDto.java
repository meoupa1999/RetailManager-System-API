package com.sonnh.retailmanagerv2.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;
@Value
public class SuggestWarehouseResDto implements Serializable {
    private UUID warehouseId;
    private Double percent;

    public SuggestWarehouseResDto(UUID warehouseId, Double percent) {
        this.warehouseId = warehouseId;
        this.percent = percent;
    }
}
