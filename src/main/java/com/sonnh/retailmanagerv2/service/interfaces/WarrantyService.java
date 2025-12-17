package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import com.sonnh.retailmanagerv2.dto.request.staff.CreateWarrantyCardReqDto;
import com.sonnh.retailmanagerv2.dto.request.staff.UpdateWarrantyCardReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.CheckWarrantyDto;
import com.sonnh.retailmanagerv2.dto.response.staff.WarrantyCardDetailResDto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface WarrantyService {
    public CheckWarrantyDto checkWarrantyByOdId(UUID orderDetailId);

    public String createWarranty(CreateWarrantyCardReqDto dto);

    public String updateWarranty(UUID warrantyCardId, UpdateWarrantyCardReqDto dto);

    public PageImplResDto getAllWarrantyCard(UUID orderDetailId, LocalDateTime afterTime,
                                             LocalDateTime beforeTime, GuarantedStatus status,
                                             Integer page, Integer size);

    public WarrantyCardDetailResDto findWarrantyCardById(UUID warrantyCardId);
}
