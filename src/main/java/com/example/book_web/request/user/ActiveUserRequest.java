package com.example.book_web.request.user;

import com.example.book_web.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActiveUserRequest {
    @NotBlank(message = MessageKeys.USER.ADDRESS_NOT_BLANK)
    private String username;

    @NotBlank(message = MessageKeys.USER.USER_KEY_ACTIVE_NOT_BLANK)
    private String keyActive;
}
