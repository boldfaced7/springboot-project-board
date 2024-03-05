package com.boldfaced7.board.error.exception.article;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.entity.NotFoundException;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
