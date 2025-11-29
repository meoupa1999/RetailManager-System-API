package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.data.specification.StoreSpecification;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.StoreUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.exception.store_exception.StoreNotFoundException;
import com.sonnh.retailmanagerv2.mapper.StoreMapper;
import com.sonnh.retailmanagerv2.service.interfaces.StoreService;
import lombok.NoArgsConstructor;
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
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;


    public PageImplResDto getAllStore(String name, String phone, String mail, String address, String ward, String district, String province, Integer page, Integer size) {
        Specification<Store> spec = Specification.where((null));
        if (StringUtils.hasText(name)) {
            spec = spec.and(StoreSpecification.nameContains(name));
        }

        if (StringUtils.hasText(phone)) {
            spec = spec.and(StoreSpecification.phoneContains(phone));
        }

        if (StringUtils.hasText(mail)) {
            spec = spec.and(StoreSpecification.mailContains(mail));
        }

        if (StringUtils.hasText(address)) {
            spec = spec.and(StoreSpecification.addressContains(address));
        }

        if (StringUtils.hasText(ward)) {
            spec = spec.and(StoreSpecification.wardContains(ward));
        }

        if (StringUtils.hasText(district)) {
            spec = spec.and(StoreSpecification.districtContains(district));
        }

        if (StringUtils.hasText(province)) {
            spec = spec.and(StoreSpecification.provinceContains(province));
        }

        spec = spec.and(StoreSpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<Store> storePage = this.storeRepository.findAll(spec, pageable);
        Page<StoreResDto> dto = storePage.map(storeMapper::toStoreResDto);
        return PageImplResDto.fromPage(dto);
    }

    public StoreDetailResDto findStoreById(UUID id) throws StoreNotFoundException {
        Store entity = this.storeRepository.findById(id).filter(store -> store.getAudit().getIsActive()).orElseThrow(handlerStoreNotFound);
//        StoreMapper var10001 = this.storeMapper;
//        Objects.requireNonNull(var10001);
        return Optional.ofNullable(entity).map(storeMapper::toStoreDetailResDto).get();
    }

    @Transactional
    public UUID createStore(StoreCreateReqDto dto) {
        Store entity = Optional.ofNullable(dto).map(storeMapper::toStoreCreateEntity).get();
//        StoreMapper var10001 = this.storeMapper;
//        Objects.requireNonNull(var10001);
//        Store entity = (Store)var10000.map(var10001::toStoreCreateEntity).get();
//        Store entity =  dtoOptional.
        return entity.getId();
    }

    @Transactional
    public UUID updateStore(UUID storeId, StoreUpdateReqDto dto) {
        Store entity = this.storeRepository.findById(storeId).filter(store ->
                        store.getAudit().getIsActive())
                       .orElseThrow(handlerStoreNotFound);
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

        if (!Objects.isNull(dto.getDescription())) {
            entity.setDescription(dto.getDescription());
        }

        if (!Objects.isNull(dto.getProvince())) {
            entity.setProvince(dto.getProvince());
        }

        if (!Objects.isNull(dto.getDistrict())) {
            entity.setDistrict(dto.getDistrict());
        }

        if (!Objects.isNull(dto.getWard())) {
            entity.setWard(dto.getWard());
        }

        if (!Objects.isNull(dto.getPhone())) {
            entity.setPhone(dto.getPhone());
        }

        if (!Objects.isNull(dto.getMail())) {
            entity.setMail(dto.getMail());
        }

        return storeRepository.save(entity).getId();
    }

    //-----------------------
    private final Supplier<StoreNotFoundException> handlerStoreNotFound = () ->
         new StoreNotFoundException("The Store Isn't Exsist");
}
