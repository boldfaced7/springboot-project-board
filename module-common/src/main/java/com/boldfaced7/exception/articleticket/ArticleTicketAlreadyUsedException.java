package com.boldfaced7.exception.articleticket;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.entity.NotFoundException;

public class ArticleTicketAlreadyUsedException extends NotFoundException {

    public ArticleTicketAlreadyUsedException() {
        super(ErrorCode.ARTICLE_TICKET_ALREADY_USED);
    }
}
