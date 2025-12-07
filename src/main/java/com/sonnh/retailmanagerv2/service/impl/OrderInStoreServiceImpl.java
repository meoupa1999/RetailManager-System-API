package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.bussiness.InventoryRedis;
import com.sonnh.retailmanagerv2.data.domain.*;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.data.repository.AccountRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreInventoryRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.dto.request.customer.CreateDraftOrderReqDto;
import com.sonnh.retailmanagerv2.dto.response.customer.DraftOrderResDto;
import com.sonnh.retailmanagerv2.security.StoreContextDetail;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderInStoreServiceImpl implements OrderInStoreService {
    private final StoreRepository storeRepository;
    private final AccountRepository accountRepository;
    private final StoreInventoryRepository storeInventoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public DraftOrderResDto createDraftOrder(CreateDraftOrderReqDto dto) {
        Long totalPriceBeforeDiscount = 0L;
        Long totalDiscountProduct = 0L;
        Long finalPrice = 0L;
        UUID draftId = UUID.randomUUID();
        System.out.println("Draft Id: " + draftId);
        List<UUID> promotionUUIDList = new ArrayList<>();
        List<DraftOrderResDto.OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        DraftOrderResDto resultDto = new DraftOrderResDto();
        resultDto.setDraftId(draftId);
        resultDto.setPromotionUUIDList(promotionUUIDList);
        resultDto.setOrderDetailDtoList(orderDetailDtoList);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        StoreContextDetail context = (StoreContextDetail) auth.getDetails();
        UUID storeId = context.getStoreID();
        List<InventoryRedis> inventoryRedisList = getInventoryInRedis(storeId);
        Store store = storeRepository.findById(storeId).get();
        // set store info
        resultDto.setStoreId(storeId);
        resultDto.setStoreName(store.getName());
        resultDto.setStoreAdress(store.getAddress());
        resultDto.setStorePhone(store.getPhone());
        resultDto.setStoreMail(store.getMail());
        //set customer info
        Account customer = accountRepository.findById(dto.getCustomerId()).get();
        resultDto.setCustomerId(dto.getCustomerId());
        resultDto.setCustomerName(customer.getName());
        resultDto.setCustomerAddress(customer.getAddress());
        resultDto.setCustomerPhone(customer.getPhone());
        //set info chung của order
        resultDto.setDescription(dto.getDescription());
        System.out.println("chay toi day 01");
        //xu li order detail dto
        for (CreateDraftOrderReqDto.ProductDto data : dto.getProductDtoList()) {
            InventoryRedis inventoryRedis = inventoryRedisList.stream()
                    .filter(ir -> ir.getProductId().equals(data.getProductId()))
                    .findAny().get();
            StoreInventory product = null;
            DraftOrderResDto.OrderDetailDto orderDetailDto;
            if (inventoryRedis.getQuantity() >= data.getQuantity()) {
                product = storeInventoryRepository.findById(data.getProductId()).get();
                Long quantity = data.getQuantity();
                orderDetailDto = new DraftOrderResDto.OrderDetailDto();
                orderDetailDto.setProductId(data.getProductId());
                orderDetailDto.setProductName(product.getName());
                orderDetailDto.setQuantity(quantity);
                Long unitPrice = product.getPrice();
                orderDetailDto.setUnitPrice(unitPrice);
                Long orginalPrice = product.getPrice() * quantity;
                totalPriceBeforeDiscount += orginalPrice;
                orderDetailDto.setOriginalPrice(orginalPrice);
                System.out.println("chay toi day 02");
                // xu li promotion all store
                Set<Promotion> promotionSet = product.getPromotionList();
                promotionSet
                        .addAll(product.getStore_storeInventoryList().stream()
                                .flatMap(ssi -> ssi.getPromotionStoreStoreInventoryList().stream())
                                .collect(Collectors.toSet()));

                Promotion promotion = promotionSet.stream()
                        .filter(p -> p.getAudit().getIsActive() && p.getStatus().equals(PromotionStatus.ACTIVE))
                        .peek(p -> System.out.println("Promotion: " + p.getName()))
                        .sorted(Comparator.comparing(p -> calculatorPromotion((Promotion) p, unitPrice, quantity)).reversed())
                        .findFirst().orElse(null);
                System.out.println("chay toi day 03");
                Long discountAmount = 0L;
                if (promotion != null) {
                    System.out.println("chay vo promotion");
                    discountAmount = calculatorPromotion(promotion, unitPrice, quantity);
                    totalDiscountProduct += discountAmount;
                    orderDetailDto.setDiscountAmount(discountAmount);
                    promotionUUIDList.add(promotion.getId());
                }
                System.out.println("chay vo day 04");
                Long totalPrice = orginalPrice - discountAmount;
                System.out.println("chay vo day 05");
                finalPrice += totalPrice;
                System.out.println("chay vo day 06");
                orderDetailDto.setTotalPrice(totalPrice);
                // xu li tru quantity trong redis
                inventoryRedis.setQuantity(inventoryRedis.getQuantity() - data.getQuantity());
                inventoryRedisList.set(inventoryRedisList.indexOf(inventoryRedis), inventoryRedis);
            } else
                orderDetailDto = new DraftOrderResDto.OrderDetailDto(null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
            orderDetailDtoList.add(orderDetailDto);
            System.out.println("chay vo day 07");
            resultDto.setTotalPriceBeforeDiscount(totalPriceBeforeDiscount);
            resultDto.setTotalDiscountProduct(totalDiscountProduct);
            resultDto.setFinalPrice(finalPrice);
            updateInventoryInRedis(storeId,inventoryRedisList);
        }
        createDraftInRedis(draftId, resultDto);
        return resultDto;
    }

    //------------
    public Long calculatorPromotion(Promotion promotion, Long unitPrice, Long quantity) {
        Long discountAmmount =
                (BigDecimal.valueOf(unitPrice)
                        .multiply(promotion.getDiscountPercent().divide(BigDecimal.valueOf(100))))
                        .multiply(BigDecimal.valueOf(quantity)).longValue();
        if (promotion.getMaxDiscountAmount() != null && discountAmmount > promotion.getMaxDiscountAmount())
            return promotion.getMaxDiscountAmount();
        return discountAmmount;

    }

    public void createInventoryFromDbToRedis(UUID storeId) {
        List<Store_StoreInventory> storeStoreInventories = storeRepository.findById(storeId).get().getStoreStoreInventoryList();
        List<InventoryRedis> inventoryRedisList = storeStoreInventories.stream()
                .map(ssi -> new InventoryRedis(ssi.getStoreInventory().getId(), ssi.getQuantity()))
                .collect(Collectors.toList());
        String redisInventoryKey = "inventory:" + storeId;
        redisTemplate.opsForValue().set(redisInventoryKey, inventoryRedisList);
    }

    public void createDraftInRedis(UUID draftId, DraftOrderResDto dto) {
        System.out.println("chay vo draft redis");
        String redisDraftKey = "draft:" + draftId;
        redisTemplate.opsForValue().set(redisDraftKey, dto,30,TimeUnit.MINUTES);
    }

    public List<InventoryRedis> getInventoryInRedis(UUID storeId) {
        String redisInventoryKey = "inventory:" + storeId;
        return (List<InventoryRedis>) redisTemplate.opsForValue().get(redisInventoryKey);
    }

    public void updateInventoryInRedis(UUID storeId,List<InventoryRedis> inventoryRedisList) {
        String redisInventoryKey = "inventory:" + storeId;
        redisTemplate.opsForValue().set(redisInventoryKey, inventoryRedisList);
    }
}
