package com.sonnh.retailmanagerv2.dto.response.staff;

import com.sonnh.retailmanagerv2.data.domain.enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.WarrantyCard}
 */
@Data
@NoArgsConstructor
public class WarrantyCardDetailResDto implements Serializable {
    UUID id;
    String description;
    LocalDateTime startWarrantyTime;
    LocalDateTime completeWarrantyTime;
    GuarantedStatus status;
    OrderDetailDto orderDetail;

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.OrderDetail}
     */
    @Data
    @NoArgsConstructor
    public static class OrderDetailDto implements Serializable {
        private UUID id;
        private Long quantity;
        private Long unitPrice;
        private Long originalPrice;
        private Long discountAmount;
        private Long totalPrice;
        private OrdersDto order;
        private StoreInventoryDto product;

        /**
         * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Orders}
         */
        @Data
        @NoArgsConstructor
        public static class OrdersDto implements Serializable {
            private UUID id;
            private String shipAddress;
            private String shipPhone;
            private Long totalPriceBeforeDiscount;
            private Long totalDiscountProduct;
            private Long totalDiscountRank;
            private Long finalPrice;
            private OrderType orderType;
            private PaymentMethod paymentMethod;
            private String description;
            private RankStatus rankApplied;
            private OrderStatus status;
        }

        /**
         * DTO for {@link com.sonnh.retailmanagerv2.data.domain.StoreInventory}
         */
        @Data
        @NoArgsConstructor
        public static class StoreInventoryDto implements Serializable {
            private UUID id;
            private String productCode;
            private String name;
        }
    }
}