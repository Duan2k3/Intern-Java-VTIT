package com.example.book_web.components;

import com.example.book_web.service.NotificationService;
import com.example.book_web.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class NotificationComponent {

    private final NotificationService notificationService;


    @Scheduled(cron = "0 16 14 * * ?")
    public void scheduleScan() {
        notificationService.scanAndUpdateOverdueBooks();
    }

    @Scheduled(cron = "0 17 14 * * ?")
    public void scheduleSendEmail() {
        notificationService.sendOverdueEmails();
    }
}
