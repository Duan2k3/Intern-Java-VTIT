package com.example.book_web.Exception;

public class FileProcessException extends RuntimeException{
    private final String code;

    public FileProcessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
