package com.boldfaced7.board.common.exception.exception.articlecomment;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.ErrorResponse;
import com.boldfaced7.board.common.exception.exception.GlobalExceptionHandler;
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
