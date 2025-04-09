package com.example.book_web.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class UserListResponse {
    private List<UserResponseForKeyWord> users;
    private int totalPages;
    private int currentPages;
}
