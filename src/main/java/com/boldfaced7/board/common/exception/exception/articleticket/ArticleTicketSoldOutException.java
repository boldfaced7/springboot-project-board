package com.boldfaced7.board.common.exception.exception.articleticket;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.entity.NotFoundException;

public class ArticleTicketSoldOutException extends NotFoundException {

    public ArticleTicketSoldOutException() {
        super(ErrorCode.ARTICLE_TICKET_SOLD_OUT);
    }
}
