package com.boldfaced7.board.common.exception.exception.attachment;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.ErrorResponse;
import com.boldfaced7.board.common.exception.exception.GlobalExceptionHandler;
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
