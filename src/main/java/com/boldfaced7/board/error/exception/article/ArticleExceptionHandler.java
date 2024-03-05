package com.boldfaced7.board.error.exception.article;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.ErrorResponse;
import com.boldfaced7.board.error.exception.GlobalExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ArticleExceptionHandler {

    @ExceptionHandler(ArticleNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handle(ArticleNotFoundException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
