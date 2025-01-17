package com.boldfaced7.common.exception.exception.exception.articleticket;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.entity.NotFoundException;

public class ArticleTicketSoldOutException extends NotFoundException {

    public ArticleTicketSoldOutException() {
        super(ErrorCode.ARTICLE_TICKET_SOLD_OUT);
    }
}
