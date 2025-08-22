package com.example.book_web.dto.book;

import lombok.Data;

@Data
public class FilterBookDTO {
    private String title;
    private String authors;
    private String description;
    private Integer quantity;

}
