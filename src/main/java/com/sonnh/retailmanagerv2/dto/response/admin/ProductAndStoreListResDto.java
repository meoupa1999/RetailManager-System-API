package com.sonnh.retailmanagerv2.dto.response.admin;

import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.StoreInventory}
 */
@Data
@NoArgsConstructor
public class ProductAndStoreListResDto implements Serializable,PromotionDetailResDto {
    private UUID promotionId;
    private String name;
    private Double discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PromotionStatus status;
    private PromotionType type;

    List<ProductDto> productDtoList;

    @Data
    public static class ProductDto {

        private UUID id;
        private String productCode;
        private String name;
        private String brand;
        private List<StoreDto> storeDtoList;

        public ProductDto(UUID id, String productCode, String name, String brand) {
            this.id = id;
            this.productCode = productCode;
            this.name = name;
            this.brand = brand;
        }


        @Data
        @NoArgsConstructor
        public static class StoreDto implements Serializable {
            private UUID id;
            private String name;
            private String address;
            private String phone;
            private String mail;

            public StoreDto(UUID id, String name, String address, String phone, String mail) {
                this.id = id;
                this.name = name;
                this.address = address;
                this.phone = phone;
                this.mail = mail;
            }
        }
    }
}