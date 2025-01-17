package com.boldfaced7.common.exception.exception.exception.attachment;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.ErrorResponse;
import com.boldfaced7.common.exception.exception.exception.GlobalExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AttachmentExceptionHandler {
    @ExceptionHandler(AttachmentNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handle(AttachmentNotFoundException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.ATTACHMENT_NOT_FOUND);
    }
}
