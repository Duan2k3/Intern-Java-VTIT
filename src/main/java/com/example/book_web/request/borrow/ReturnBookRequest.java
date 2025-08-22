package com.example.book_web.request.borrow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnBookRequest {
    @JsonProperty("borrow_id")
    private Long borrowId;

    @JsonProperty("book_id")
    private List<Long> bookIds;
}
