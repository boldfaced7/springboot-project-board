package com.boldfaced7.board.error.exception.attachment;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.ErrorResponse;
import com.boldfaced7.board.error.exception.GlobalExceptionHandler;
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
