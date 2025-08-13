package com.example.book_web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CategoryDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}
