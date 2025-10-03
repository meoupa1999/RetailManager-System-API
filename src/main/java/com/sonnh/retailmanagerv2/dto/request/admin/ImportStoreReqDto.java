package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Data
public class ImportStoreReqDto implements Serializable {
  private   List<UUID> warehouseIdList;
   private List<ProductImportStoreDto> productImportStoreDtoList;

    public ImportStoreReqDto(List<UUID> warehouseIdList, List<ProductImportStoreDto> productImportStoreDtoList) {
        this.warehouseIdList = warehouseIdList;
        this.productImportStoreDtoList = productImportStoreDtoList;
    }

    @Data
    @ToString
    public static class ProductImportStoreDto implements Serializable {
        private UUID productId;
        private Long quantity;

        public ProductImportStoreDto(UUID productId, Long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            ProductImportStoreDto product = (ProductImportStoreDto) obj;
            return Objects.equals(productId, product.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId);                   // chỉ hash id
        }
    }
}
