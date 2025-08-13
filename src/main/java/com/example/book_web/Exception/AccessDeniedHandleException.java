package com.example.book_web.Exception;

public class AccessDeniedHandleException extends RuntimeException{
    private  final  String code;
    public AccessDeniedHandleException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
