package com.sonnh.retailmanagerv2.data.domain.enums;

public enum GuarantedStatus {
//    NOT_WARRANTIED,
//    IN_WARRANTY,
//    WARRANTIED,
//    EXPIRED;

    WARRANTING,  //đang bảo hành
    COMPLETED,   // đã bảo hành xong
    VALID,       // được bảo hành ( sẵn sàng để bảo hành)
    EXPIRED;  // hết hạn bảo hành

}
