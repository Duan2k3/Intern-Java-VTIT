package com.example.book_web.dto.book;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookPageCacheDTO implements Serializable {
    private List<BookDTO> content;
    private long totalElements;
    private int totalPages;
}
