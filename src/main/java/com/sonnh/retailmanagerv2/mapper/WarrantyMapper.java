package com.sonnh.retailmanagerv2.mapper;

import com.sonnh.retailmanagerv2.data.domain.Guaranted;
import com.sonnh.retailmanagerv2.data.domain.OrderDetail;
import com.sonnh.retailmanagerv2.data.domain.WarrantyCard;
import com.sonnh.retailmanagerv2.dto.request.staff.UpdateWarrantyCardReqDto;
import com.sonnh.retailmanagerv2.dto.response.staff.CheckWarrantyDto;
import com.sonnh.retailmanagerv2.dto.response.staff.WarrantyCardDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.WarrantyCardResDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarrantyMapper {

    CheckWarrantyDto tocheckWarrantyDto(OrderDetail orderDetail);

    WarrantyCard toWarrantyCardEntity(UpdateWarrantyCardReqDto updateWarrantyCardReqDto);


    WarrantyCardResDto toWarrantyCardResDto(WarrantyCard warrantyCard);


    WarrantyCardDetailResDto toWarrantyCardDetailResDto(WarrantyCard warrantyCard);


}