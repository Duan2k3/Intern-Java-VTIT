package com.example.book_web.dto;

import com.example.book_web.enums.BorrowStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowHistoryDTO {

    @JsonProperty("borrow_id")
    private Long id;
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;
    @JsonProperty("returned_date")
    private LocalDate returnedDate;
    @JsonProperty("actual_returned_date")
    private LocalDate actualReturnedDate;
    @JsonProperty("title")
    private String bookTitle;
    @JsonProperty("status")
    private String status;

}
