package com.example.book_web.Exception;

import lombok.Data;

@Data
public class IllegalArgumentException extends RuntimeException{
    private final String code;

    public IllegalArgumentException(String code, String message) {
        super(message);
        this.code = code;
    }


}
