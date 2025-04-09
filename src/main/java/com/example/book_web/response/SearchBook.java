package com.example.book_web.response;

import com.example.book_web.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class SearchBook {
    private List<Book> books;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
