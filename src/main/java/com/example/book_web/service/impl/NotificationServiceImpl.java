package com.example.book_web.service.impl;

import com.example.book_web.entity.BorrowDetail;
import com.example.book_web.entity.Notification;
import com.example.book_web.repository.BorrowDetailRepository;
import com.example.book_web.repository.NotificationRepository;
import com.example.book_web.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final BorrowDetailRepository borrowDetailRepository;
    private final EmailServiceImpl mailService;

    @Override
    @Transactional
    public void scanAndUpdateOverdueBooks() {
        List<BorrowDetail> overdueDetails = borrowDetailRepository.findOverdueUnreturnedDetails();

        for (BorrowDetail detail : overdueDetails) {
            createNotificationIfNotExists(detail);
        }

        List<BorrowDetail> returnedDetails = borrowDetailRepository.findReturnedWithNotification();

        for (BorrowDetail detail : returnedDetails) {
            removeNotificationAndSendThankYou(detail);
        }
    }

    private void createNotificationIfNotExists(BorrowDetail detail) {
        boolean exists = notificationRepository.existsByBorrowDetailId(detail.getId());
        if (exists) return;

        Notification notification = Notification.builder()
                .borrowDetailId(detail.getId())
                .userId(detail.getBorrow().getUser().getId())
                .bookId(detail.getBook().getId())
                .dueDate(detail.getBorrow().getReturnDate())
                .timesNotified(0)
                .lastNotifiedAt(null)
                .notified(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    private void removeNotificationAndSendThankYou(BorrowDetail detail) {
        notificationRepository.deleteByBorrowDetailId(detail.getId());
        mailService.sendThankYouNotification(detail);
    }

    @Override
    @Transactional
    public void sendOverdueEmails() {
        List<Notification> notifications = notificationRepository.findNotNotifiedToday();

        for (Notification notification : notifications) {
            BorrowDetail detail = borrowDetailRepository.findById(notification.getBorrowDetailId()).orElse(null);
            if (detail == null || detail.getActualReturnedDate() != null) continue;

            mailService.sendOverdueEmail(notification);

            notification.setLastNotifiedAt(LocalDate.now());
            notification.setTimesNotified(notification.getTimesNotified() + 1);
            notification.setNotified(true);

            notificationRepository.save(notification);
        }
    }
}
