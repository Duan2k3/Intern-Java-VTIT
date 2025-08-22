package com.example.book_web.dto;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowDetailDTO {
    @NotNull(message = MessageKeys.BORROW.BORROW_USER_ID_NOT_NULL)
    private Long bookId;

    @NotNull(message = MessageKeys.BOOK.BOOK_QUANTITY_NOT_NULL)
    private Integer quantity;
}
