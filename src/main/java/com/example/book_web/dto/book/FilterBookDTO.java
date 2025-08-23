package com.example.book_web.dto.book;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterBookDTO {
    private String title;
    private String authors;
    private String description;
    private Integer quantity;
    private String image;

    public FilterBookDTO(String title, String authors, String description, Integer quantity, String image) {
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.quantity = quantity;
        this.image = image;
    }

}
