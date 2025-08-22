package com.example.book_web.dto.user;

import com.example.book_web.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UserDTO {
    @NotBlank(message = MessageKeys.USER.USER_NAME_NOT_BLANK)
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Chỉ chấp nhận email Gmail")
    private String email;

    @NotBlank(message = MessageKeys.USER.FULLNAME_NOT_BLANK)
    private String fullname;

    @NotBlank(message = MessageKeys.USER.PHONE_NUMBER_NOT_BLANK)
    private String phoneNumber;

    @NotBlank(message = MessageKeys.USER.IDENTITY_NUMBER_NOT_BLANK)
    private String identityNumber;

    @NotNull(message = MessageKeys.USER.AGE_NOT_NULL)
    private Long age;

    private LocalDate DateOfBirth;

    @NotBlank(message = MessageKeys.USER.ADDRESS_NOT_BLANK)
    private String address;

}
