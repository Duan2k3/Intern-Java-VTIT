package com.example.book_web.dto.book;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    private String title;

    private String image;

    private String authors;

    private Integer quantity;

    private String description;

    private List<Long> categoriesIds;
}
