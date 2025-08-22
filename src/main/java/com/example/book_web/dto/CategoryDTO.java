package com.example.book_web.dto;


import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data

public class CategoryDTO {

    private String name;
}
