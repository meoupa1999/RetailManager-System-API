package com.sonnh.retailmanagerv2.dto.request.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDraftOrderReqDto implements Serializable {
    private UUID customerId;
    private String description;
    private List<ProductDto> productDtoList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDto implements Serializable {
        private UUID productId;
        private Long quantity;
    }


}
