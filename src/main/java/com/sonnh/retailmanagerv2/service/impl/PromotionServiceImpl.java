package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import com.sonnh.retailmanagerv2.data.repository.PromotionRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.data.repository.Store_StoreInventoryRepository;
import com.sonnh.retailmanagerv2.data.specification.StoreInventoryByProductSpecification;
import com.sonnh.retailmanagerv2.data.specification.StoreSpecification;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreByProductResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.mapper.StoreMapper;
import com.sonnh.retailmanagerv2.service.interfaces.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final Store_StoreInventoryRepository store_storeInventoryRepository;
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    @Override
    @Transactional
    public String createPromotion(PromotionCreateReqDto dto) {

        Promotion promotion = new Promotion();
        promotion.setName(dto.getName());
        promotion.setDiscountPercent(dto.getDiscountPercent());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotionRepository.save(promotion);

        for (PromotionCreateReqDto.ProductDto productDto :dto.getProductDtoList()) {
            UUID productId = productDto.getProductId();
            for (UUID storeId : productDto.getStoreIdList()) {
                Store_StoreInventory store_storeInventory = store_storeInventoryRepository
                                                            .findStoreStoreInventory(storeId,productId);
                store_storeInventory.addPromotion(promotion);
            }
        }
        return "Success";
    }

    @Override
    public PageImplResDto getStoreByProductId(UUID productId, String address, String name, String phone, Integer page, Integer size) {
        Specification<Store> spec = Specification.where((null));
            spec = spec.and(StoreInventoryByProductSpecification.hasProductId(productId));

        if (StringUtils.hasText(address)) {
            spec = spec.and(StoreInventoryByProductSpecification.addressContains(address));
        }

        if (StringUtils.hasText(name)) {
            spec = spec.and(StoreInventoryByProductSpecification.nameContains(name));
        }

        if (StringUtils.hasText(phone)) {
            spec = spec.and(StoreInventoryByProductSpecification.phoneContains(phone));
        }

        spec = spec.and(StoreInventoryByProductSpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<Store> storePage = this.storeRepository.findAll(spec, pageable);
        System.out.println("Size: " + storePage.getContent().size());
        Page<StoreByProductResDto> dto = storePage.map(storeMapper::toStoreByProductResDto);
        return PageImplResDto.fromPage(dto);
    }




}
