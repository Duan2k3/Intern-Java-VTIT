package com.example.book_web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TestDto {
    private Long id;
    private String title;
    private String authors;
    private Integer quantity;
    private String email;
    private String username;

    public TestDto(Long id, String title, String authors, Integer quantity, String email, String username) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.quantity = quantity;
        this.email = email;
        this.username = username;
    }
}
