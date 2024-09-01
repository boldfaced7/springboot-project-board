package com.boldfaced7.board.common.exception.exception.articleticket;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.ErrorResponse;
import com.boldfaced7.board.common.exception.exception.GlobalExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ArticleTicketExceptionHandler {

    @ExceptionHandler(ArticleTicketNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handle(ArticleTicketNotFoundException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.ARTICLE_TICKET_NOT_FOUND);
    }
    @ExceptionHandler(ArticleTicketSoldOutException.class)
    protected ResponseEntity<ErrorResponse> handle(ArticleTicketSoldOutException e) {
        return GlobalExceptionHandler.createErrorResponseEntity(ErrorCode.ARTICLE_TICKET_SOLD_OUT);
    }
}
