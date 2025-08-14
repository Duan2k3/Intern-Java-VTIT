package com.example.book_web.dto;


import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CategoryDTO {

    @NotNull(message = MessageKeys.CATEGORY.CATEGORY_NAME_NOT_BLANK )
    @JsonProperty("name")
    private String name;
}
