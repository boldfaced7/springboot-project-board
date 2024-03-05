package com.boldfaced7.board.error.exception.auth;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.ErrorResponse;
import com.boldfaced7.board.error.exception.GlobalExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<ErrorResponse> handle(ForbiddenException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> handle(UnauthorizedException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAuthValueException.class)
    protected ResponseEntity<ErrorResponse> handle(InvalidAuthValueException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.INVALID_AUTH_VALUE);
    }
}
