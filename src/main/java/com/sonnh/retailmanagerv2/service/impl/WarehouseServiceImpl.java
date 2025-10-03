package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Warehouse;
import com.sonnh.retailmanagerv2.data.repository.WarehouseRepository;
import com.sonnh.retailmanagerv2.data.specification.WarehouseSpecification;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseResDto;
import com.sonnh.retailmanagerv2.exception.warehouse_exception.WarehouseNotFoundException;
import com.sonnh.retailmanagerv2.mapper.WarehouseMapper;
import com.sonnh.retailmanagerv2.service.interfaces.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    public PageImplResDto getAllWarehouse(String name, String address, String phone, String email, String description, Integer page, Integer size) {
        Specification<Warehouse> spec = Specification.where((null));
        if (StringUtils.hasText(name)) {
            spec = spec.and(WarehouseSpecification.nameContains(name));
        }

        if (StringUtils.hasText(address)) {
            spec = spec.and(WarehouseSpecification.addressContains(address));
        }

        if (StringUtils.hasText(phone)) {
            spec = spec.and(WarehouseSpecification.phoneContains(phone));
        }

        if (StringUtils.hasText(email)) {
            spec = spec.and(WarehouseSpecification.emailContains(email));
        }

        if (StringUtils.hasText(description)) {
            spec = spec.and(WarehouseSpecification.descriptionContains(description));
        }

        spec = spec.and(WarehouseSpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<Warehouse> warehousePage = this.warehouseRepository.findAll(spec, pageable);
//        WarehouseMapper var10001 = this.warehouseMapper;
//        Objects.requireNonNull(var10001);
        Page<WarehouseResDto> dto = warehousePage.map(warehouseMapper::toWarehouseResDto);
        return PageImplResDto.fromPage(dto);
    }

    public WarehouseDetailResDto findWarehouseById(UUID id) {
        Warehouse entity = this.warehouseRepository.findById(id).filter(warehouse ->
                warehouse.getAudit().getIsActive()).orElseThrow();
//        WarehouseMapper var10001 = this.warehouseMapper;
//        Objects.requireNonNull(var10001);
        return Optional.ofNullable(entity).map(warehouseMapper::toWarehouseDetailResDto).get();
    }

    @Transactional
    public UUID createWarehouse(WarehouseCreateReqDto dto) {
        Warehouse entity = Optional.ofNullable(dto).map(warehouseMapper::toWarehouseEntity).get();
//        WarehouseMapper var10001 = this.warehouseMapper;
//        Objects.requireNonNull(var10001);
        return warehouseRepository.save(entity).getId();
    }

    @Transactional
    public UUID updateWarehouse(UUID id, WarehouseUpdateReqDto dto) {
        Warehouse entity = warehouseRepository.findById(id).filter(warehouse ->
                        warehouse.getAudit().getIsActive())
                       .orElseThrow(this.handlerWarehouseNotFound);
        if (!Objects.isNull(dto.getName())) {
            entity.setName(dto.getName());
        }

        if (!Objects.isNull(dto.getAddress())) {
            entity.setAddress(dto.getAddress());
        }

        if (!Objects.isNull(dto.getLatitude())) {
            entity.setLatitude(dto.getLatitude());
        }

        if (!Objects.isNull(dto.getLongitude())) {
            entity.setLongitude(dto.getLongitude());
        }

        if (!Objects.isNull(dto.getPhone())) {
            entity.setPhone(dto.getPhone());
        }

        if (!Objects.isNull(dto.getEmail())) {
            entity.setEmail(dto.getEmail());
        }

        if (!Objects.isNull(dto.getDescription())) {
            entity.setDescription(dto.getDescription());
        }

        return warehouseRepository.save(entity).getId();
    }

    //-------------
    private final Supplier<WarehouseNotFoundException> handlerWarehouseNotFound = () -> {
        return new WarehouseNotFoundException("The Warehouse Isn't Exsist");
    };

}
