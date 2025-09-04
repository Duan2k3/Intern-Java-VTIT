package com.example.book_web.request.category;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotNull(message = MessageKeys.CATEGORY.CATEGORY_NAME_NOT_BLANK )
    @Max(value = 100, message = MessageKeys.CATEGORY.CATEGORY_NAME_TOO_LONG)
    private String name;
}
