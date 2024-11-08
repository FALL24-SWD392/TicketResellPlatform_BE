package com.swd392.ticket_resell_be.utils;

import com.swd392.ticket_resell_be.services.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleTask {
    TokenService tokenService;
    TransactionService transactionService;
    TicketService ticketService;
    UserService userService;
    MembershipService membershipService;

    @Scheduled(cron = "0 */10 * * * *")
    public void deleteExpiredToken() {
        tokenService.deleteExpiredToken();
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void deleteUnverifiedUser() {
        userService.deleteUnverifiedUser();
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void updatePendingToFailed() {
        transactionService.updatePendingToFailed();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateExpiredTicket() {
        ticketService.updateExpiredTicket();
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void createInitialMembership() {
        membershipService.createInitialMembership();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateExpiredMembership() {
        membershipService.updateExpiredMembership();
    }

}
