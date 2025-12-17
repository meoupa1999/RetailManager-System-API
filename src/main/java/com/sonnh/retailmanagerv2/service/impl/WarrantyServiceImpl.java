package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Guaranted;
import com.sonnh.retailmanagerv2.data.domain.OrderDetail;
import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.WarrantyCard;
import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import com.sonnh.retailmanagerv2.data.repository.GuarantedRepository;
import com.sonnh.retailmanagerv2.data.repository.OrderDetailRepository;
import com.sonnh.retailmanagerv2.data.repository.WarrantyCardRepository;
import com.sonnh.retailmanagerv2.data.specification.StoreSpecification;
import com.sonnh.retailmanagerv2.data.specification.WarrantyCardSpecification;
import com.sonnh.retailmanagerv2.dto.request.staff.CreateWarrantyCardReqDto;
import com.sonnh.retailmanagerv2.dto.request.staff.UpdateWarrantyCardReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.CheckWarrantyDto;
import com.sonnh.retailmanagerv2.dto.response.staff.WarrantyCardDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.staff.WarrantyCardResDto;
import com.sonnh.retailmanagerv2.mapper.WarrantyMapper;
import com.sonnh.retailmanagerv2.service.interfaces.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class WarrantyServiceImpl implements WarrantyService {
    private final WarrantyMapper warrantyMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final GuarantedRepository guarantedRepository;
    private final WarrantyCardRepository warrantyCardRepository;
    @Override
    public CheckWarrantyDto checkWarrantyByOdId(UUID orderDetailId) {
        return orderDetailRepository.findById(orderDetailId).map(warrantyMapper::tocheckWarrantyDto).get();
    }

    @Override
    @Transactional
    public String createWarranty(CreateWarrantyCardReqDto dto) {
        System.out.println("run vao day createWarranty");
        OrderDetail orderDetail = orderDetailRepository.findById(dto.getOrderDetailId()).get();
        Guaranted guaranted = guarantedRepository.findById(dto.getWarrantyId()).get();
        WarrantyCard warrantyCard = new WarrantyCard();
        warrantyCard.setDescription(dto.getDescription());
        warrantyCard.setStartWarrantyTime(dto.getStartWarrantyTime());
        warrantyCard.setCompleteWarrantyTime(dto.getCompleteWarrantyTime());
        warrantyCard.setStatus(GuarantedStatus.WARRANTING);
        warrantyCard.addOrderDetail(orderDetail);
        warrantyCard.addGuaranted(guaranted);
        warrantyCardRepository.save(warrantyCard);
        return "Success";
    }

    @Override
    @Transactional
    public String updateWarranty(UUID warrantyCardId,UpdateWarrantyCardReqDto dto) {
        WarrantyCard warrantyCard = warrantyCardRepository.findById(warrantyCardId).get();
        warrantyCard = Optional.ofNullable(dto).map(warrantyMapper::toWarrantyCardEntity).get();
        return "Success";
    }

    @Override
    public PageImplResDto getAllWarrantyCard(UUID orderDetailId, LocalDateTime afterTime,
                                             LocalDateTime beforeTime, GuarantedStatus status,
                                             Integer page, Integer size) {

        Specification<WarrantyCard> spec = Specification.where((null));
        spec = spec.and(WarrantyCardSpecification.hasStoreId());
        if (orderDetailId != null) {
            spec = spec.and(WarrantyCardSpecification.hasOrderDetailId(orderDetailId));
        }

        if (afterTime != null) {
            spec = spec.and(WarrantyCardSpecification.afterTime(afterTime));

        }

        if (beforeTime != null) {
            spec = spec.and(WarrantyCardSpecification.beforeTime(beforeTime));
        }

        if (status != null) {
            spec = spec.and(WarrantyCardSpecification.hasStatus(status));
        }
//        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"startWarrantyTime"}));
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100);
        Page<WarrantyCard> warrantyCardPage = this.warrantyCardRepository.findAll(spec, pageable);
        System.out.println("in ra size của warranty card : " + warrantyCardPage.getTotalElements());
        Page<WarrantyCardResDto> dto = warrantyCardPage.map(warrantyMapper::toWarrantyCardResDto);
        return PageImplResDto.fromPage(dto);
    }

    @Override
    public WarrantyCardDetailResDto findWarrantyCardById(UUID warrantyCardId) {
        Optional<WarrantyCard> warrantyCard = warrantyCardRepository.findById(warrantyCardId);
        System.out.println("check thu " + warrantyCard.get().getId());
        return warrantyCard.map(warrantyMapper::toWarrantyCardDetailResDto).get();
    }
}
