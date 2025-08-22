package com.example.book_web.request.borrow_detail;

import com.example.book_web.utils.MessageKeys;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowDetailRequest {

    @NotNull(message = MessageKeys.BORROW.BORROW_USER_ID_NOT_NULL)
    private Long bookId;

    @NotNull(message = MessageKeys.BOOK.BOOK_QUANTITY_NOT_NULL)
    private Integer quantity;
}
