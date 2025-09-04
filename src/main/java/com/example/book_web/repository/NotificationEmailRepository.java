package com.example.book_web.repository;


import com.example.book_web.entity.NotificationEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEmailRepository extends JpaRepository<NotificationEmail,Long> {
}
