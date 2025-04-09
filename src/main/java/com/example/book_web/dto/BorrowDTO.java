package com.example.book_web.dto;

import com.example.book_web.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
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
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("borrow_date")
    private LocalDate borrowDate;
    @JsonProperty("returned_date")
    private LocalDate returnDate;

    private List<BorrowDetailDTO> borrowDetails;
}
