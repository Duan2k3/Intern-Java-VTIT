package com.example.book_web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_job")
@Getter
@Setter
public class NotificationEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String type;
    private String status;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    private String message;
    private String error;


}

