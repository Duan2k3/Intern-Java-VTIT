package com.example.book_web.request.borrow;

import com.example.book_web.dto.BorrowDetailDTO;
import com.example.book_web.request.borrow_detail.BorrowDetailRequest;
import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BorrowRequest {

    @NotNull(message = MessageKeys.BORROW.BORROW_USER_ID_NOT_NULL)
    private Long userId;

    @NotNull(message = MessageKeys.BORROW.BORROW_DATE_NOT_NULL)
    private LocalDate borrowDate;

    @NotNull(message = MessageKeys.BORROW.BORROW_RETURN_DATE_NOT_NULL)
    private LocalDate returnDate;

    private List<BorrowDetailRequest> borrowDetails;
}
