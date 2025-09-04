package com.example.book_web.request.user;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

    @NotBlank(message = MessageKeys.USER.USER_NAME_NOT_BLANK)
    @Size(max = 200, message = MessageKeys.USER.USERNAME_TOO_LONG)
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Chỉ chấp nhận email Gmail")
    private String email;

    @NotBlank(message = MessageKeys.USER.PASSWORD_NOT_BLANK)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_\\-+={}\\[\\]|\\\\:;\"'<>,.?/]).{8,}$",
            message = "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
    )
    private String password;

    @NotBlank(message = MessageKeys.USER.FULLNAME_NOT_BLANK)
    @Size(max = 100, message = MessageKeys.USER.FULLNAME_TOO_LONG)
    private String fullname;

    @NotBlank(message = MessageKeys.USER.PHONE_NUMBER_NOT_BLANK)
    @Pattern(regexp = "\\d{10}", message = MessageKeys.USER.PHONE_NUMBER_INVALID)
    private String phoneNumber;

    @NotBlank(message = MessageKeys.USER.IDENTITY_NUMBER_NOT_BLANK)
    private String identityNumber;

    @NotNull(message = MessageKeys.USER.AGE_NOT_NULL)
    @Min(value = 0, message = MessageKeys.USER.AGE_NOT_INVALID)
    private Long age;

    private LocalDate DateOfBirth;

    @NotBlank(message = MessageKeys.USER.ADDRESS_NOT_BLANK)
    private String address;

    private Long role;
}
