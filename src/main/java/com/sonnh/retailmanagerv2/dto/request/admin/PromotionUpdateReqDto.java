package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PromotionUpdateReqDto implements Serializable {
    private String name;
    private Double discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ProductDto> productDtoList;


    @Data

    public static class ProductDto implements Serializable{
        private UUID productId;
        private List<UUID> storeIdList;

//        public ProductDto(UUID productId, List<UUID> storeIdList) {
//            this.productId = productId;
//            this.storeIdList = storeIdList;
//        }
    }
}
