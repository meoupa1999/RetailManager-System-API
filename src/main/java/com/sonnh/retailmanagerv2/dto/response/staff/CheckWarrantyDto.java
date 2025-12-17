package com.sonnh.retailmanagerv2.dto.response.staff;

import com.sonnh.retailmanagerv2.data.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.OrderDetail}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckWarrantyDto implements Serializable {
    private UUID id;
    private Long quantity;
    private Long unitPrice;
    private Long originalPrice;
    private Long discountAmount;
    private Long totalPrice;
    private OrdersDto order;
    private GuarantedDto guaranted;

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Orders}
     */
    @Data
    @AllArgsConstructor
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
        private AccountDto customer;

        /**
         * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Account}
         */
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AccountDto implements Serializable {
            private UUID id;
            private String mail;
            private String phone;
            private String username;
            private String password;
            private String name;
            private String address;
            private String note;
            private long loyaltyPoint;
            private Double latitude;
            private Double longgitude;
        }
    }

    /**
     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Guaranted}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GuarantedDto implements Serializable {
        private UUID id;
        private Integer durationTime;
        private LocalDateTime startDate;
        private LocalDateTime expiredDate;
        private GuarantedStatus guarantedStatus;
    }
}