package com.example.book_web.request.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookRequest {

    private String author;

    private String title;

    private Integer pageNumber;

    private Integer pageSize;

    private String keyword;

    private Date fromStartDate;

    private Date toStartDate;
}
