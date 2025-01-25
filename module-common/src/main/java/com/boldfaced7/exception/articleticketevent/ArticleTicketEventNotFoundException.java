package com.boldfaced7.exception.articleticketevent;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.entity.NotFoundException;

public class ArticleTicketEventNotFoundException extends NotFoundException {

    public ArticleTicketEventNotFoundException() {
        super(ErrorCode.ARTICLE_TICKET_NOT_FOUND);
    }
}
