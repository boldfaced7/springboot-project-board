package com.boldfaced7.common.exception.exception.exception.articleticket;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.entity.NotFoundException;

public class ArticleTicketNotFoundException extends NotFoundException {

    public ArticleTicketNotFoundException() {
        super(ErrorCode.ARTICLE_TICKET_NOT_FOUND);
    }
}
