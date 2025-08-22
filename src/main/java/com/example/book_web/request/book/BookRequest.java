package com.example.book_web.request.book;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Data

public class BookRequest {

    @NotBlank(message = MessageKeys.BOOK.BOOK_TITLE_NOT_BLANK)
    private String title;

    @NotBlank(message = MessageKeys.BOOK.BOOK_AUTHOR_NOT_BLANK)
    private String authors;

    @NotNull(message = MessageKeys.BOOK.BOOK_QUANTITY_NOT_NULL)
    private Integer quantity;

    @NotBlank(message = MessageKeys.BOOK.BOOK_DESCRIPTION_NOT_BLANK)
    private String description;

    private List<Long> categoriesIds;
}
