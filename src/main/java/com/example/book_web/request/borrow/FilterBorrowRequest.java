package com.example.book_web.request.borrow;


import com.example.book_web.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterBorrowRequest {

    private String id;
    private String title;
    private String author;
    private Integer quantity;
    private String email;
    private String username;
    private BorrowStatus status;

    private Integer pageNumber;

    private Integer pageSize;

    private String keyword;

    private Date fromStartDate;

    private Date toStartDate;
}
