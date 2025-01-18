package com.boldfaced7.common.exception.exception.exception.member;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.ErrorResponse;
import com.boldfaced7.common.exception.exception.exception.GlobalExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handle(MemberNotFoundException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.MEMBER_NOT_FOUND);
    }

    @ExceptionHandler(MemberEmailDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handle(MemberEmailDuplicatedException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.MEMBER_EMAIL_DUPLICATED);
    }
}