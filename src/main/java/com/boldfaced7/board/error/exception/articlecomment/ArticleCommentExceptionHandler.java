package com.boldfaced7.board.error.exception.articlecomment;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.ErrorResponse;
import com.boldfaced7.board.error.exception.GlobalExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ArticleCommentExceptionHandler {

    @ExceptionHandler(ArticleCommentNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handle(ArticleCommentNotFoundException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.ARTICLE_COMMENT_NOT_FOUND);
    }
}
