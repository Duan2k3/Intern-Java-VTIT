package com.example.book_web.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    public static final int SUCCESS_CODE = 200;
    public static final int NOT_FOUND_CODE = 404;
    public static final int ERROR_CODE = 500;

    public static final String SUCCESS_MESSAGE = "Success";
    public static final String NOT_FOUND_MESSAGE = "Not found";
    public static final String ERROR_MESSAGE = "Server error";

    @Builder.Default
    private int code = SUCCESS_CODE;
    @Builder.Default
    private String message = SUCCESS_MESSAGE;
    private T data;

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
