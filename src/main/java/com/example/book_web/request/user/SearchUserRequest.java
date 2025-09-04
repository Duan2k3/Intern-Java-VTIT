package com.example.book_web.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequest {

    private String email;

    private String fullname;

    private String phoneNumber;

    private String identityNumber;


    private LocalDate dateOfBirth;

    private String address;


    private Integer pageNumber;

    private Integer pageSize;

    private String keyword;

    private Date fromStartDate;

    private Date toStartDate;
}
