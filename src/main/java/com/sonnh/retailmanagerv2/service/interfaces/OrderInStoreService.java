package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.customer.CreateDraftOrderReqDto;
import com.sonnh.retailmanagerv2.dto.response.customer.DraftOrderResDto;

import java.util.List;
import java.util.UUID;

public interface OrderInStoreService {
    public DraftOrderResDto createDraftOrder(CreateDraftOrderReqDto dto);

    public String acceptDraftOrder(UUID draftId);

    public String cancelDraftOrder(UUID draftId);
}
