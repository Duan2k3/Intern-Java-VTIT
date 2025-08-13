package com.example.book_web.Exception;

public class DataExistingException extends RuntimeException{
    private  final  String code;
    public DataExistingException(String message,String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
