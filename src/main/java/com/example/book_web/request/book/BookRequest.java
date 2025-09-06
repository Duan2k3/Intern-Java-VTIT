package com.example.book_web.request.book;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data

public class BookRequest {

    @NotBlank(message = MessageKeys.BOOK.BOOK_TITLE_NOT_BLANK)
    @Size(max = 200, message = MessageKeys.BOOK.BOOK_TITLE_NOT_TOO_LONG)
    private String title;

    @NotBlank(message = MessageKeys.BOOK.BOOK_AUTHOR_NOT_BLANK)
    @Size(max = 100, message = MessageKeys.BOOK.BOOK_AUTHOR_NOT_TOO_LONG)
    private String authors;

    @NotNull(message = MessageKeys.BOOK.BOOK_QUANTITY_NOT_NULL)
    @Min(message = MessageKeys.BOOK.QUANTITY_NOT_SMALLER_1, value = 1)
    private Integer quantity;

    @NotBlank(message = MessageKeys.BOOK.BOOK_DESCRIPTION_NOT_BLANK)
    @Size(max = 500, message = MessageKeys.BOOK.BOOK_DESCRIPTION_TOO_LONG)
    private String description;


    @NotNull(message = MessageKeys.BOOK.BOOK_PULISH_DATE_NOT_BLANK)
    private Date publishDate;

    private List<Long> categoriesIds;
}
