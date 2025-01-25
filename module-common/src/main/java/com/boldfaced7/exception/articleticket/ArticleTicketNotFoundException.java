package com.boldfaced7.exception.articleticket;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.entity.NotFoundException;

public class ArticleTicketNotFoundException extends NotFoundException {

    public ArticleTicketNotFoundException() {
        super(ErrorCode.ARTICLE_TICKET_NOT_FOUND);
    }
}
