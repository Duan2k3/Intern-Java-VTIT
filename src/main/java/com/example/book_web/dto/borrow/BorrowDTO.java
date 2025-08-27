package com.example.book_web.dto.borrow;

import com.example.book_web.dto.borrow_detail.BorrowDetailDTO;
import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowDTO {
    @NotNull(message = MessageKeys.BORROW.BORROW_USER_ID_NOT_NULL)
    private Long userId;

    @NotNull(message = MessageKeys.BORROW.BORROW_DATE_NOT_NULL)
    private LocalDate borrowDate;

    @NotNull(message = MessageKeys.BORROW.BORROW_RETURN_DATE_NOT_NULL)
    private LocalDate returnDate;

    private List<BorrowDetailDTO> borrowDetails;
}
