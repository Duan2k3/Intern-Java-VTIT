package com.example.book_web.dto;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = MessageKeys.USER.USER_NAME_NOT_BLANK)
    private String username;

    @JsonProperty("email")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Chỉ chấp nhận email Gmail")
    private String email;


    @JsonProperty("password")
    @NotBlank(message = MessageKeys.USER.PASSWORD_NOT_BLANK)
    private String password;

    @JsonProperty("fullname")
    @NotBlank(message = MessageKeys.USER.FULLNAME_NOT_BLANK)
    private String fullname;


    @JsonProperty("phone_number")
    @NotBlank(message = MessageKeys.USER.PHONE_NUMBER_NOT_BLANK)
    private String phoneNumber;


    @JsonProperty("identity_number")
    @NotBlank(message = MessageKeys.USER.IDENTITY_NUMBER_NOT_BLANK)
    private String identityNumber;

    @NotNull(message = MessageKeys.USER.AGE_NOT_NULL)
    private Long age;

    @JsonProperty("date_of_birth")
    private LocalDate birthday;

    @NotBlank(message = MessageKeys.USER.ADDRESS_NOT_BLANK)
    private String address;


    @JsonProperty("role_id")
    private Long role;
}
