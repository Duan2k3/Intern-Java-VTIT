package com.example.book_web.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowDetailDTO {
    private Long bookId;
    private Integer quantity;
}
