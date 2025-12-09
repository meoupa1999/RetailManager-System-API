package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.bussiness.InventoryRedis;
import com.sonnh.retailmanagerv2.data.domain.*;
import com.sonnh.retailmanagerv2.data.domain.enums.OrderStatus;
import com.sonnh.retailmanagerv2.data.domain.enums.OrderType;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.data.repository.*;
import com.sonnh.retailmanagerv2.dto.request.customer.CreateDraftOrderReqDto;
import com.sonnh.retailmanagerv2.dto.response.customer.DraftOrderResDto;
import com.sonnh.retailmanagerv2.security.StoreContextDetail;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public DraftOrderResDto createDraftOrder(CreateDraftOrderReqDto dto) {
        Long totalPriceBeforeDiscount = 0L;
        Long totalDiscountProduct = 0L;
        Long finalPrice = 0L;
        UUID draftId = UUID.randomUUID();
        List<UUID> promotionUUIDList = new ArrayList<>();
        List<DraftOrderResDto.OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        DraftOrderResDto resultDto = new DraftOrderResDto();
        resultDto.setDraftId(draftId);
        resultDto.setPromotionUUIDList(promotionUUIDList);
        resultDto.setOrderDetailDtoList(orderDetailDtoList);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        StoreContextDetail context = (StoreContextDetail) auth.getDetails();
        UUID storeId = context.getStoreId();
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
        resultDto.setPaymentMethod(dto.getPaymentMethod());
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
                Long discountAmount = 0L;
                if (promotion != null) {
                    discountAmount = calculatorPromotion(promotion, unitPrice, quantity);
                    totalDiscountProduct += discountAmount;
                    orderDetailDto.setDiscountAmount(discountAmount);
                    promotionUUIDList.add(promotion.getId());
                }
                Long totalPrice = orginalPrice - discountAmount;
                finalPrice += totalPrice;
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
            updateInventoryInRedis(storeId, inventoryRedisList);
        }
        createDraftInRedis(storeId,draftId, resultDto);
        createDraftTempInRedis(draftId,resultDto);
        return resultDto;
    }

    @Override
    @Transactional
    public String acceptDraftOrder(UUID draftId) {
        String redisDraftKey = "draft:" + draftId;
        if (redisTemplate.hasKey(redisDraftKey) != null && redisTemplate.hasKey(redisDraftKey)) {
            DraftOrderResDto draft = (DraftOrderResDto) redisTemplate.opsForValue().get(redisDraftKey);
            Orders orders = new Orders();
            //set info cho order
            orders.setTotalPriceBeforeDiscount(draft.getTotalPriceBeforeDiscount());
            orders.setTotalDiscountProduct(draft.getTotalDiscountProduct());
            orders.setFinalPrice(draft.getFinalPrice());
            orders.setOrderType(OrderType.IN_STORE_PURCHASE);
            orders.setPaymentMethod(draft.getPaymentMethod());
            orders.setDescription(draft.getDescription());
            orders.setStatus(OrderStatus.COMPLETED);
            ordersRepository.save(orders);
            // set orderDetail info
            for (DraftOrderResDto.OrderDetailDto orderDetailDto : draft.getOrderDetailDtoList()) {
                OrderDetail orderDetail = new OrderDetail();
                StoreInventory product = storeInventoryRepository.findById(orderDetailDto.getProductId()).get();
                orderDetail.addProduct(product);
                orderDetail.setQuantity(orderDetailDto.getQuantity());
                orderDetail.setOriginalPrice(orderDetailDto.getOriginalPrice());
                orderDetail.setDiscountAmount(orderDetailDto.getDiscountAmount());
                orderDetail.setUnitPrice(orderDetailDto.getUnitPrice());
                orderDetail.setTotalPrice(orderDetailDto.getTotalPrice());
                orderDetail.addOrder(orders);
                orderDetailRepository.save(orderDetail);
            }
            deleteKeyValueInRedis(draftId);
        }
        return "Success";
    }

    @Override
    public String cancelDraftOrder(UUID draftId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        StoreContextDetail context = (StoreContextDetail) auth.getDetails();
        UUID storeId = context.getStoreId();
        cancelExpiredDraft(draftId,storeId);
        return "Success";
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

    public void createDraftInRedis(UUID storeId, UUID draftId, DraftOrderResDto dto) {
        String redisDraftKey = "draft:" + storeId + ":" + draftId;
        redisTemplate.opsForValue().set(redisDraftKey, dto, 30, TimeUnit.MINUTES);
    }

    public List<InventoryRedis> getInventoryInRedis(UUID storeId) {
        String redisInventoryKey = "inventory:" + storeId;
        return (List<InventoryRedis>) redisTemplate.opsForValue().get(redisInventoryKey);
    }

    public void updateInventoryInRedis(UUID storeId, List<InventoryRedis> inventoryRedisList) {
        String redisInventoryKey = "inventory:" + storeId;
        redisTemplate.opsForValue().set(redisInventoryKey, inventoryRedisList);
    }

    public void deleteKeyValueInRedis(UUID draftId) {
        String redisDraftKey = "draft:" + draftId;
        redisTemplate.delete(redisDraftKey);
    }

    public void createDraftTempInRedis(UUID draftId, DraftOrderResDto dto) {
        String redisDraftKey = "tempDraft:" + draftId;
        redisTemplate.opsForValue().set(redisDraftKey, dto);
    }

    public void cancelExpiredDraftListener(UUID draftId, UUID storeId) {
        String redisInventoryKey = "inventory:" + storeId;
        String tempDraftKey = "tempDraft:" + draftId;
        DraftOrderResDto draftOrderResDto = (DraftOrderResDto) redisTemplate.opsForValue().get(tempDraftKey);
        List<InventoryRedis> inventoryRedisList = getInventoryInRedis(storeId);
        for (DraftOrderResDto.OrderDetailDto orderDetailDto : draftOrderResDto.getOrderDetailDtoList()) {
            for (InventoryRedis  inventoryRedis: inventoryRedisList) {
                if (inventoryRedis.getProductId().equals(orderDetailDto.getProductId())) {
                    inventoryRedis.setQuantity(inventoryRedis.getQuantity() + orderDetailDto.getQuantity());
                }
            }
        }
        redisTemplate.opsForValue().set(redisInventoryKey, inventoryRedisList);
        redisTemplate.delete(tempDraftKey);
    }

    public void cancelExpiredDraft(UUID draftId, UUID storeId) {
        String redisInventoryKey = "inventory:" + storeId;
        String redisDraftKey = "draft:" + storeId + ":" + draftId;
        String tempDraftKey = "tempDraft:" + draftId;
        DraftOrderResDto draftOrderResDto = (DraftOrderResDto) redisTemplate.opsForValue().get(tempDraftKey);
        List<InventoryRedis> inventoryRedisList = getInventoryInRedis(storeId);
        for (DraftOrderResDto.OrderDetailDto orderDetailDto : draftOrderResDto.getOrderDetailDtoList()) {
            for (InventoryRedis  inventoryRedis: inventoryRedisList) {
                if (inventoryRedis.getProductId().equals(orderDetailDto.getProductId())) {
                    inventoryRedis.setQuantity(inventoryRedis.getQuantity() + orderDetailDto.getQuantity());
                }
            }
        }
        redisTemplate.opsForValue().set(redisInventoryKey, inventoryRedisList);
        redisTemplate.delete(redisDraftKey);
        redisTemplate.delete(tempDraftKey);
    }
}
