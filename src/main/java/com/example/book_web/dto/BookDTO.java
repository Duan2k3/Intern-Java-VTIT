package com.example.book_web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String authors;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("description")
    private String description;

    private List<Long> categoriesIds;
}
