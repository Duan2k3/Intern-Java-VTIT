package com.example.book_web.dto;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    @NotBlank(message = MessageKeys.BOOK.BOOK_TITLE_NOT_BLANK)
    @JsonProperty("title")
    private String title;

    @JsonProperty("image")
    private String image;

    @NotBlank(message = MessageKeys.BOOK.BOOK_AUTHOR_NOT_BLANK)
    @JsonProperty("author")
    private String authors;

    @NotNull(message = MessageKeys.BOOK.BOOK_QUANTITY_NOT_NULL)
    @JsonProperty("quantity")
    private Integer quantity;

    @NotBlank(message = MessageKeys.BOOK.BOOK_DESCRIPTION_NOT_BLANK)
    @JsonProperty("description")
    private String description;

    private List<Long> categoriesIds;
}
