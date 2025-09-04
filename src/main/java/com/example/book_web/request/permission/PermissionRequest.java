package com.example.book_web.request.permission;

import com.example.book_web.utils.MessageKeys;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionRequest {

    private String name;

    @NotBlank(message = MessageKeys.PERMISSION.PERMISSION_DESCRIPTION_NOT_BLANK)
    @Max(value = 200, message = MessageKeys.PERMISSION.PERMISSION_DESCRIPTION_TOO_LONG)
    private String description;
}
