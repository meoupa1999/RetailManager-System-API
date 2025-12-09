package com.sonnh.retailmanagerv2.scheduler;

import com.sonnh.retailmanagerv2.security.StoreContextDetail;
import com.sonnh.retailmanagerv2.service.impl.OrderInStoreServiceImpl;
import com.sonnh.retailmanagerv2.service.interfaces.OrderInStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    private OrderInStoreServiceImpl orderInStoreService;


    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        System.out.println("EXPIRED KEY = " + expiredKey);
        String[] parts = expiredKey.split(":");
        UUID storeId = UUID.fromString(parts[1]);
        UUID draftId = UUID.fromString(parts[2]);
        if (expiredKey.startsWith("draft:")) {
            orderInStoreService.cancelExpiredDraftListener(draftId, storeId);
        }
    }
}
