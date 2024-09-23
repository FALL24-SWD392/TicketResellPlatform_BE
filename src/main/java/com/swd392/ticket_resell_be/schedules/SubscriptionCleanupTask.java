package com.swd392.ticket_resell_be.schedules;

import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionCleanupTask {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionCleanupTask(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredSubscriptions() {
        List<Subscription> expiredSubscriptions = subscriptionRepository.findByEndDateBeforeAndIsActive(LocalDate.now(), true);
        for (Subscription subscription : expiredSubscriptions) {
            subscription.setActive(false); // Hoặc bạn có thể xóa nó
            subscriptionRepository.save(subscription); // Cập nhật trạng thái
        }
    }
}
