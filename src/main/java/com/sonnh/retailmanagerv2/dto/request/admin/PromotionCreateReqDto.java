package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PromotionCreateReqDto implements Serializable {
    private String name;
    private BigDecimal discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ProductDto> productDtoList;

    public PromotionCreateReqDto(String name,
                                 BigDecimal discountPercent,
                                 LocalDateTime startDate,
                                 LocalDateTime endDate,
                                 List<ProductDto> productDtoList) {
        this.name = name;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.productDtoList = productDtoList;
    }

    @Data
    public static class ProductDto implements Serializable{
        private UUID productId;
        private List<UUID> storeIdList;

        public ProductDto(UUID productId, List<UUID> storeIdList) {
            this.productId = productId;
            this.storeIdList = storeIdList;
        }
    }
}
