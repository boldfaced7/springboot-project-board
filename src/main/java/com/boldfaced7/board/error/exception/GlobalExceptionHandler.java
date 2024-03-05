package com.boldfaced7.board.error.exception;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        String message = messageSource.getMessage(e.getFieldErrors().get(0), Locale.KOREA);
        return createValidationErrorResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {
        return createErrorResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception e) {
        e.printStackTrace();
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode),
                errorCode.getStatus()
        );
    }

    public static ResponseEntity<ErrorResponse> createValidationErrorResponseEntity(String errorMessage, HttpStatus status) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorMessage, "VALIDATION"),
                status
        );
    }

    /*
    @ExceptionHandler()
    protected ResponseEntity<ErrorResponse> handle() {
        return createErrorResponseEntity();
    }
     */
}
