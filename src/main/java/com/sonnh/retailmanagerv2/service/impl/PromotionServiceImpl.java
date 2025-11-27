package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.domain.StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.Store_StoreInventory;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionType;
import com.sonnh.retailmanagerv2.data.repository.PromotionRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreInventoryRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.data.repository.Store_StoreInventoryRepository;
import com.sonnh.retailmanagerv2.data.specification.*;
import com.sonnh.retailmanagerv2.dto.request.admin.CreatePromotionAllStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.PromotionUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.*;
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

import java.util.*;


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
        promotion.setStatus(PromotionStatus.PENDING);
        promotion.setType(PromotionType.BY_STORE);
        promotionRepository.save(promotion);

        for (PromotionCreateReqDto.ProductDto productDto : dto.getProductDtoList()) {
            UUID productId = productDto.getProductId();
            for (UUID storeId : productDto.getStoreIdList()) {
                Store_StoreInventory store_storeInventory = store_storeInventoryRepository
                        .findStoreStoreInventory(storeId, productId);
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
        promotion.setType(PromotionType.ALL_STORE);
        promotionRepository.save(promotion);
        for (UUID productId : dto.getProductIdList()) {
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
    public ProductAndStoreListResDto getProductAndStoreByPromotionId(UUID promotionId) {
        return toProductAndStoreListResDto(promotionId);
    }

    @Override
    @Transactional
    public String updatePromotion(UUID promotionId, PromotionUpdateReqDto dto) {
        List<Store_StoreInventory> tempRemoveList = new ArrayList<>();
        Promotion promotion = promotionRepository.findById(promotionId).get();
        if (dto.getName() != null)
            promotion.setName(dto.getName());
        if (dto.getDiscountPercent() != null)
            promotion.setDiscountPercent(dto.getDiscountPercent());
        if (dto.getStartDate() != null)
            promotion.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null)
            promotion.setEndDate(dto.getEndDate());
        Set<Store_StoreInventory> ssiDtoList = new HashSet<>();
        for (PromotionUpdateReqDto.ProductDto productDto : dto.getProductDtoList()) {
            UUID productId = productDto.getProductId();
            for (UUID storeId : productDto.getStoreIdList()) {
                Store_StoreInventory store_storeInventory = store_storeInventoryRepository
                        .findStoreStoreInventory(storeId, productId);
                ssiDtoList.add(store_storeInventory);
            }
        }
        for (Store_StoreInventory ssi : promotion.getStore_storeInventoryList()) {
            if (!ssiDtoList.contains(ssi)) {
                tempRemoveList.add(ssi);
            }
        }

        for (Store_StoreInventory ssi : tempRemoveList) {
            ssi.removePromotion(promotion);
        }
//        for(Store_StoreInventory ssi : promotion.getStore_storeInventoryList()) {
//            System.out.println("check " + !ssiDtoList.contains(ssi));
//            if(!ssiDtoList.contains(ssi)) {
//                ssi.removePromotion(promotion);
//            }
//        }
        for (Store_StoreInventory ssi : ssiDtoList) {
            if (!promotion.getStore_storeInventoryList().contains(ssi))
                ssi.addPromotion(promotion);
        }
        return "update Success";
    }

    @Override
    public PromotionDetailResDto getPromotionDetail(UUID promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId).get();
        if (promotion.getType().equals(PromotionType.BY_STORE)) {
            return toProductAndStoreListResDto(promotionId);
        }
        return Optional.ofNullable(promotion)
                .map(promotionMapper::toPromotionDetailAllStoreResDto).get();
    }

    @Override
    @Transactional
    public String deletePromotion(UUID promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId).get();
        promotion.getAudit().setIsActive(false);
        return "Delete Successful";
    }


    //******************************************


    public ProductAndStoreListResDto toProductAndStoreListResDto(UUID promotionId) {
        List<ProductAndStoreListResDto.ProductDto> resultList = new ArrayList<>();
        List<StoreInventory> productList = storeInventoryRepository.findProductsByPromotionId(promotionId);
        Promotion promotion = promotionRepository.findById(promotionId).get();
        ProductAndStoreListResDto dto = new ProductAndStoreListResDto();
        dto.setPromotionId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDiscountPercent(promotion.getDiscountPercent());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setStatus(promotion.getStatus());
        dto.setType(promotion.getType());
//        for (StoreInventory data: productList) {
//            System.out.println(data.getId());
//        }


        for (StoreInventory product : productList) {
            ProductAndStoreListResDto.ProductDto productDto =
                    new ProductAndStoreListResDto.ProductDto(product.getId(), product.getProductCode(), product.getName(), product.getBrand());
            List<Store> storeList = storeRepository.findStoreByProductIdAndPromotionId(product.getId(), promotionId);
            List<ProductAndStoreListResDto.ProductDto.StoreDto> storeDtoList = new ArrayList<>();
            for (Store store : storeList) {
                ProductAndStoreListResDto.ProductDto.StoreDto storeDto =
                        new ProductAndStoreListResDto.ProductDto.StoreDto(store.getId(), store.getName(), store.getAddress(), store.getPhone(), store.getMail());
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
        dto.setProductDtoList(resultList);
        return dto;
    }
}
