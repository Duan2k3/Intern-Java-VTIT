package com.example.book_web.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private int currentPage;

    private int pageSize;

    private int totalPages;

    private long totalElements;

    private List<T> data;
}
