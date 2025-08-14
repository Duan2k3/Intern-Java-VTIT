package com.example.book_web.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private Long id;
    private String token;
    private String tokenType;
    private LocalDateTime expiration;
    private LocalDateTime expired;
    private String refreshToken;
    private LocalDateTime refreshTokenDate;
    private Long userId;
}