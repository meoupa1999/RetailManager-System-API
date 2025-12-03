package com.sonnh.retailmanagerv2.dto.request.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDraftOrderReqDto implements Serializable {
    private UUID productId;
    private Long quantity;


}
