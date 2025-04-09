package com.example.book_web.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnBookDTO {
    private LocalDate actualReturnedDate;
    private List<Long> borrowDetailIds;
}
