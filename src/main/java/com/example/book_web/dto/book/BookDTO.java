package com.example.book_web.dto.book;

import com.example.book_web.dto.category.CategoryDTO;
import com.example.book_web.entity.Category;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
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

    private Date publishDate;

    private List<Long> categoriesIds;
}
