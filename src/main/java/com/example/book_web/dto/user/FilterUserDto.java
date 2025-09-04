package com.example.book_web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterUserDto {

    private String email;

    private String fullname;

    private String phoneNumber;

    private String identityNumber;


    private LocalDate dateOfBirth;

    private String address;

    private Integer totalCount;
}
