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

    private Long userId;

    private LocalDate borrowDate;

    private LocalDate returnDate;

    private List<BorrowDetailDTO> borrowDetails;
}
