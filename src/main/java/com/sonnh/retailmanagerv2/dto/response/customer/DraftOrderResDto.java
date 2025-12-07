package com.sonnh.retailmanagerv2.dto.response.customer;

import com.sonnh.retailmanagerv2.data.domain.enums.OrderType;
import com.sonnh.retailmanagerv2.data.domain.enums.PaymentMethod;
import com.sonnh.retailmanagerv2.data.domain.enums.RankStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class DraftOrderResDto implements Serializable {
    private UUID draftId;
    private UUID storeId;
    private String storeName;
    private String storeAdress;
    private String storeMail;
    private String storePhone;
    private UUID customerId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String description;
    private Long totalPriceBeforeDiscount;
    private Long totalDiscountProduct;
    private Long totalDiscountRank;
    private Long finalPrice;
    private OrderType orderType = OrderType.IN_STORE_PURCHASE;
    private PaymentMethod paymentMethod;
    private RankStatus rankApplied;
    List<OrderDetailDto> orderDetailDtoList;
    List<UUID> promotionUUIDList;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDetailDto implements Serializable{
        private UUID productId;
        private String productName;
        private Long quantity;
        private Long unitPrice;
        private Long originalPrice;
        private Long discountAmount;
        private Long totalPrice;
         }

}
