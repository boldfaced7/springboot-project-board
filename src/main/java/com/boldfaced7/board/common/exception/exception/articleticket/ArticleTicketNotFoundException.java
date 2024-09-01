package com.boldfaced7.board.common.exception.exception.articleticket;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.entity.NotFoundException;

public class ArticleTicketNotFoundException extends NotFoundException {

    public ArticleTicketNotFoundException() {
        super(ErrorCode.ARTICLE_TICKET_NOT_FOUND);
    }
}
