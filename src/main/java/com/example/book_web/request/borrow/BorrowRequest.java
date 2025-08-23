package com.example.book_web.request.borrow;

import com.example.book_web.request.borrow_detail.BorrowDetailRequest;
import com.example.book_web.utils.MessageKeys;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BorrowRequest {

    @NotNull(message = MessageKeys.BORROW.BORROW_DATE_NOT_NULL)
    private LocalDate borrowDate;

    @NotNull(message = MessageKeys.BORROW.BORROW_RETURN_DATE_NOT_NULL)
    private LocalDate returnDate;

    private String note;

    private List<BorrowDetailRequest> borrowDetails;
}
