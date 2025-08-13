package com.example.book_web.common;

import com.example.book_web.Base.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConfig<T> {
    public static final String SUCCESS_CODE = "00";

    public static ResponseEntity error(HttpStatus httpStatus, String errorCode, String message) {
        ResponseDto responseData = ResponseDto.builder().code(errorCode).message(message).build();
        return new ResponseEntity(responseData, httpStatus);
    }
    public static <T> ResponseEntity<ResponseDto<T>> success(T body) {
        ResponseDto responseDto = ResponseDto.builder().data(body).code(SUCCESS_CODE).build();
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

}
