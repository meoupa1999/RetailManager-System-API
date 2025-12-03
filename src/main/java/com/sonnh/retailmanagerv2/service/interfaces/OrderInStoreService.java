package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.customer.CreateDraftOrderReqDto;
import com.sonnh.retailmanagerv2.dto.response.customer.DraftOrderResDto;

import java.util.List;

public interface OrderInStoreService {
    public DraftOrderResDto createDraftOrder(List<CreateDraftOrderReqDto> dtoList);
}
