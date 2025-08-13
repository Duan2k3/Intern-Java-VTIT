package com.example.book_web.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
@Builder

public class Notification {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "borrow_detail_id" , nullable = false)
        private Long borrowDetailId;

        @Column(name = "user_id",nullable = false)
        private Long userId;


        @Column(name = "times_notified")
        private int timesNotified;



        @Column(name = "last_notified_at")
        private LocalDate lastNotifiedAt;

        @Column(name = "book_id")
        private Long bookId;

        @Column(name = "due_date")
        private LocalDate dueDate;

        @Column(name = "notified")
        private Boolean notified;

        @Column(name = "created_at")
        private LocalDateTime createdAt;


}
