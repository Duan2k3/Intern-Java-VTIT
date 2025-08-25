package com.example.book_web.Exception;

import com.example.book_web.common.MessageCommon;
import com.example.book_web.common.ResponseConfig;
import jakarta.validation.ConstraintViolation;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class ExceptionHandingController {

    @Autowired
    MessageCommon messageCommon;

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseBody
    public ResponseEntity handleCategoriesException(CategoryNotFoundException ex) {
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
    public ResponseEntity handleDataExistingException(DataExistingException ex) {
        return ResponseConfig.error(BAD_REQUEST, ex.getCode(), ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();

            String message = messageCommon.getMessage(error.getDefaultMessage());

            Map<String, Object> attributes =
                    error.unwrap(ConstraintViolation.class).getConstraintDescriptor().getAttributes();

            String formattedMessage = mapAttribute(message, attributes);

            errors.put(fieldName, formattedMessage);
        }

        String firstError = errors.values().stream().findFirst().orElse(null);
        return ResponseConfig.error(BAD_REQUEST, "400", firstError);
    }
    private String mapAttribute(String message, Map<String, Object> attributes) {
        if (message == null || attributes == null)
            return message;

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            message = message.replace("{" + key + "}", value);
        }

        return message;
    }

    @ExceptionHandler(JRException.class)
    public ResponseEntity<String> handleJasperException(JRException ex) {
        return ResponseConfig.error(FORBIDDEN, "500", "Có lỗi JasperReports: " + ex.getMessage());
    }
    @ExceptionHandler(FileProcessException.class)
    public ResponseEntity<String> handleIOException(Exception ex) {
        return ResponseConfig.error(FORBIDDEN, "500", "Có lỗi xảy ra: " + ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseConfig.error(FORBIDDEN, "403", "Đăng nhập thất bạii: " );
    }
}
