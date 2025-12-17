package com.sonnh.retailmanagerv2.scheduler;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import com.sonnh.retailmanagerv2.data.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionCronJob {
    private final PromotionRepository promotionRepository;
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh") // chay vao 12h dem
//    @Scheduled(cron = "*/10 * * * * *",zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void runCronJob() {
        System.out.println("chay cron ");
        List<Promotion> promotionList = promotionRepository.findAll();
        for (Promotion promotion : promotionList) {
            if (promotion.getStartDate().toLocalDate().isBefore(LocalDateTime.now().toLocalDate())) {
                promotion.setStatus(PromotionStatus.ACTIVE);
            }
            if (promotion.getEndDate().toLocalDate().isBefore(LocalDateTime.now().toLocalDate() )
                && promotion.getStartDate().isBefore(LocalDateTime.now())) {
                promotion.setStatus(PromotionStatus.EXPIRED);
            }
        }
    }
}
