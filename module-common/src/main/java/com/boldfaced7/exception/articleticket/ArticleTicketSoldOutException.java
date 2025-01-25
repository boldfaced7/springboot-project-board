package com.boldfaced7.exception.articleticket;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.entity.NotFoundException;

public class ArticleTicketSoldOutException extends NotFoundException {

    public ArticleTicketSoldOutException() {
        super(ErrorCode.ARTICLE_TICKET_SOLD_OUT);
    }
}
