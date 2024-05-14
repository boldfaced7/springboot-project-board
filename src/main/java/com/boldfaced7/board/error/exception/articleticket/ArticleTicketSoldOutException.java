package com.boldfaced7.board.error.exception.articleticket;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.entity.NotFoundException;

public class ArticleTicketSoldOutException extends NotFoundException {

    public ArticleTicketSoldOutException() {
        super(ErrorCode.ARTICLE_TICKET_SOLD_OUT);
    }
}
