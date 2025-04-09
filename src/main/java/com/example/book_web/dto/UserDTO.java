package com.example.book_web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


import java.time.LocalDate;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UserDTO {

    @JsonProperty("user_name")
    private String userName;



    @JsonProperty("password")
    private String password;

    @JsonProperty("fullname")
    private String fullname;


    @JsonProperty("phone_number")
    private String phoneNumber;


    @JsonProperty("identity_number")
    private String identityNumber;


    private Long age;


    @JsonProperty("date_of_birth")
    private LocalDate birthday;


    private String address;


    @JsonProperty("role_id")
    private Long role;
}
