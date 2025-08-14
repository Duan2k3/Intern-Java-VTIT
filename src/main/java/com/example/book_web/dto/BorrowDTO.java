package com.example.book_web.dto;

import com.example.book_web.entity.User;
import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
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
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = MessageKeys.BORROW.BORROW_DATE_NOT_NULL)
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;

    @NotNull(message = MessageKeys.BORROW.BORROW_RETURN_DATE_NOT_NULL)
    @JsonProperty("returned_date")
    private LocalDate returnDate;

    private List<BorrowDetailDTO> borrowDetails;
}
