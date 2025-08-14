package com.example.book_web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250)
    private String token;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column
    private LocalDateTime expiration;

    @Column
    private LocalDateTime expired;

    @Column(name = "refresh_token", length = 250)
    private String refreshToken;

    @Column(name = "refresh_token_date")
    private LocalDateTime refreshTokenDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt ;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Integer isDeleted ;
}