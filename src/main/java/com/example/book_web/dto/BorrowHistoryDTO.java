package com.example.book_web.dto;

import com.example.book_web.enums.BorrowStatus;
import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = MessageKeys.BORROW_HISTORY.BORROW_DATE_NOT_NULL)
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @NotNull(message = MessageKeys.BORROW_HISTORY.RETURNED_DATE_NOT_NULL)
    @JsonProperty("returned_date")
    private LocalDate returnedDate;

    @JsonProperty("actual_returned_date")
    private LocalDate actualReturnedDate;

    @NotNull(message = MessageKeys.BORROW_HISTORY.TITLE_NOT_NULL)
    @JsonProperty("title")
    private String bookTitle;
    @JsonProperty("status")
    private String status;

}
