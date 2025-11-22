package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.StoreInventory}
 */
@Data
@NoArgsConstructor
public class ProductAndStoreListResDto implements Serializable {
    private UUID id;
    private String productCode;
    private String name;
    private String brand;
    private List<StoreDto> storeDtoList;

    public ProductAndStoreListResDto(UUID id, String productCode, String name, String brand) {
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