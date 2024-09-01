package com.boldfaced7.board.common.exception.exception.article;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.ErrorResponse;
import com.boldfaced7.board.common.exception.exception.GlobalExceptionHandler;
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
