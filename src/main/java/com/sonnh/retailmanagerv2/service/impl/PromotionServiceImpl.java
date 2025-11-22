package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.data.repository.PromotionRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreInventoryRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.data.repository.Store_StoreInventoryRepository;
import com.sonnh.retailmanagerv2.data.specification.*;
import com.sonnh.retailmanagerv2.dto.request.admin.CreatePromotionAllStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductAndStoreListResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductByPromotionResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.PromotionsResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreByProductResDto;
import com.sonnh.retailmanagerv2.mapper.PromotionMapper;
import com.sonnh.retailmanagerv2.mapper.StoreMapper;
import com.sonnh.retailmanagerv2.service.interfaces.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final Store_StoreInventoryRepository store_storeInventoryRepository;
    private final StoreRepository storeRepository;
    private final StoreInventoryRepository storeInventoryRepository;
    private final StoreMapper storeMapper;
    private final PromotionMapper promotionMapper;
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

    @Override
    @Transactional
    public String createPromotionAllStore(CreatePromotionAllStoreReqDto dto) {
        Promotion promotion = Optional.ofNullable(dto).map(promotionMapper::toPromotionEntity).get();
        promotion.setStatus(PromotionStatus.PENDING);
        promotionRepository.save(promotion);
        for (UUID productId :dto.getProductIdList()) {
            StoreInventory storeInventory = storeInventoryRepository.findById(productId).get();
            promotion.addProduct(storeInventory);
        }
        return "Success";
    }

    @Override
    public PageImplResDto getAllPromotion(String name, PromotionStatus status, Integer page, Integer size) {
        Specification<Promotion> spec = Specification.where((null));


        if (StringUtils.hasText(name)) {
            spec = spec.and(PromotionSpecification.nameContains(name));
        }

        if (status != null) {
            spec = spec.and(PromotionSpecification.statusFilter(status));
        }

        spec = spec.and(PromotionSpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<Promotion> promotionPage = this.promotionRepository.findAll(spec, pageable);
        Page<PromotionsResDto> dto = promotionPage.map(promotionMapper::toPromotionsResDto);
        return PageImplResDto.fromPage(dto);
    }

    @Override
    public PageImplResDto getProductsByPromotionId(UUID promotionId, String code, String name, String brand, Integer page, Integer size) {
        Specification<StoreInventory> spec = Specification.where((null));
        spec = spec.and(ProductByPromotionSpecification.hasPromotionId(promotionId));

        if (StringUtils.hasText(code)) {
            spec = spec.and(ProductByPromotionSpecification.productCodeContains(code));
        }
        if (StringUtils.hasText(name)) {
            spec = spec.and(ProductByPromotionSpecification.nameContains(name));
        }
        if (StringUtils.hasText(brand)) {
            spec = spec.and(ProductByPromotionSpecification.brandContains(brand));
        }
        spec = spec.and(ProductByPromotionSpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<StoreInventory> storeInventoryPage = this.storeInventoryRepository.findAll(spec, pageable);
        Page<ProductByPromotionResDto> dto = storeInventoryPage.map(promotionMapper::toProductByPromotionResDto);
        return PageImplResDto.fromPage(dto);
    }



//    @Override
//    public PageImplResDto getProductAndStoreByPromotionId(UUID promotionId, Integer page, Integer size) {
//        Specification<StoreInventory> spec = Specification.where((null));
//        spec = spec.and(ProductAndStoreListSpecification.hasPromotionId(promotionId));
//        spec = spec.and(ProductByPromotionSpecification.isActive());
//        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
//        Page<StoreInventory> storeInventoryPage = this.storeInventoryRepository.findAll(spec, pageable);
//        System.out.println("Day ne " + storeInventoryPage.getContent().size());
//        Page<ProductAndStoreListResDto> dto = storeInventoryPage.map(promotionMapper::toProductAndStoreListResDto);
//        return PageImplResDto.fromPage(dto);
//
//    }

    @Override
    public List<ProductAndStoreListResDto> getProductAndStoreByPromotionId(UUID promotionId) {
        return toProductAndStoreListResDto(promotionId);
    }


    //******************************************


    public List<ProductAndStoreListResDto> toProductAndStoreListResDto(UUID promotionId) {
        List<ProductAndStoreListResDto> resultList = new ArrayList<>();
        List<StoreInventory> productList = storeInventoryRepository.findProductsByPromotionId(promotionId);
//        for (StoreInventory data: productList) {
//            System.out.println(data.getId());
//        }



        for (StoreInventory product : productList) {
            ProductAndStoreListResDto productDto =
                    new ProductAndStoreListResDto(product.getId(),product.getProductCode(),product.getName(),product.getBrand());
            List<Store> storeList = storeRepository.findStoreByProductIdAndPromotionId(product.getId(),promotionId);
            List<ProductAndStoreListResDto.StoreDto> storeDtoList = new ArrayList<>();
            for (Store store : storeList) {
                ProductAndStoreListResDto.StoreDto storeDto =
                        new ProductAndStoreListResDto.StoreDto(store.getId(),store.getName(), store.getAddress(),store.getPhone(),store.getMail());
            storeDtoList.add(storeDto);
            }
            productDto.setStoreDtoList(storeDtoList);
            System.out.println("ProductId: " + product.getId());
            for (Store store : storeList) {
                System.out.println("StoreId: " + store.getId());
            }
            resultList.add(productDto);
            System.out.println("--------------");
        }
         return resultList;
    }
}
