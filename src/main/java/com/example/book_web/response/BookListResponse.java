package com.example.book_web.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class BookListResponse {
    private List<BookResponse> products;
    private int totalPages;
}
