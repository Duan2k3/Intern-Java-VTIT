package com.example.book_web.repository;

import com.example.book_web.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    boolean existsByBorrowDetailId(Long borrowDetailId);
    void deleteByBorrowDetailId(Long borrowDetailId);;

    Optional<Notification> findByBorrowDetailId(Long borrowDetailId);

    @Query("SELECT n FROM Notification n WHERE n.lastNotifiedAt IS NULL OR n.lastNotifiedAt <> CURRENT_DATE")
    List<Notification> findNotNotifiedToday();

}
