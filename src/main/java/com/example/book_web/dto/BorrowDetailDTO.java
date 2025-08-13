package com.example.book_web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowDetailDTO {
    @JsonProperty("book_id")
    private Long bookId;
    private Integer quantity;
}
