package com.sonnh.retailmanagerv2.dto.response.staff;

import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.WarrantyCard}
 */
@Data
public class WarrantyCardResDto implements Serializable {
   private UUID id;
   private LocalDateTime startWarrantyTime;
   private LocalDateTime completeWarrantyTime;
   private GuarantedStatus status;
   private OrderDetailDto orderDetail;

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.OrderDetail}
     */
    @Data
    public static class OrderDetailDto implements Serializable {
       private UUID id;
       private StoreInventoryDto product;

        /**
         * DTO for {@link com.sonnh.retailmanagerv2.data.domain.StoreInventory}
         */
        @Data
        public static class StoreInventoryDto implements Serializable {
           private UUID id;
           private String productCode;
           private String name;
        }
    }
}