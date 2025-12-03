package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.dto.request.customer.CreateDraftOrderReqDto;
import com.sonnh.retailmanagerv2.dto.response.customer.DraftOrderResDto;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderInStoreServiceImpl implements OrderInStoreService {

    @Override
    public DraftOrderResDto createDraftOrder(List<CreateDraftOrderReqDto> dtoList) {
        return null;
    }
}
