package com.boldfaced7.board.error.exception.articleticket;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.entity.NotFoundException;

public class ArticleTicketNotFoundException extends NotFoundException {

    public ArticleTicketNotFoundException() {
        super(ErrorCode.ARTICLE_TICKET_NOT_FOUND);
    }
}
