package com.example.book_web.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDetailResponse {
    private String name;
    private String message;
}
