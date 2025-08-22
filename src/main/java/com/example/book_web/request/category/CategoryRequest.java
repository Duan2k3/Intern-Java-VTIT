package com.example.book_web.request.category;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotNull(message = MessageKeys.CATEGORY.CATEGORY_NAME_NOT_BLANK )
    @JsonProperty("name")
    private String name;
}
