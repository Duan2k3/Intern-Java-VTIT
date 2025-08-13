package com.example.book_web.Exception;

import com.example.book_web.common.ResponseConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class ExceptionHandingController {

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseBody
    public ResponseEntity handleAccessDeniedException(CategoryNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    public ResponseEntity handleDataNotFoundException(DataNotFoundException ex) {
        return ResponseConfig.error(BAD_REQUEST, ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedHandleException.class)
    @ResponseBody
    public ResponseEntity handleAccessDeniedException(AccessDeniedHandleException ex){
        return ResponseConfig.error(BAD_REQUEST, ex.getCode(), ex.getMessage());

    }

    @ExceptionHandler(DataExistingException.class)
    @ResponseBody
    public ResponseEntity handleDataNotFoundException(DataExistingException ex) {
        return ResponseConfig.error(BAD_REQUEST, ex.getCode(), ex.getMessage());
    }

}
