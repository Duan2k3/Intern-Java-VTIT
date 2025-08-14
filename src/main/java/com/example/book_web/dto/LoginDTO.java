package com.example.book_web.dto;

import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginDTO {

    @NotBlank(message = MessageKeys.LOGIN.USER_NAME_NOT_BLANK)
    @JsonProperty("user_name")
    private String userName;

    @NotBlank(message = MessageKeys.LOGIN.PASSWORD_NOT_BLANK)
    @JsonProperty("password")
    private String password;
}
