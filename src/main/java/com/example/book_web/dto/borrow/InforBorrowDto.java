package com.example.book_web.dto.borrow;

import com.example.book_web.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InforBorrowDto {
    private Long id;
    private String title;
    private String author;
    private Integer quantity;
    private String email;
    private String userName;
    private BorrowStatus status;
    private Integer totalCount;


}
