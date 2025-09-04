package com.example.book_web.dto.book;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FilterBookDTO {
    private Long id;
    private String title;
    private String author;
    private String description;
    private Integer quantity;
    private String image;
    private Integer totalCount;

    public FilterBookDTO(Long id ,String title, String author, String description, Integer quantity, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.quantity = quantity;
        this.image = image;
    }

}
