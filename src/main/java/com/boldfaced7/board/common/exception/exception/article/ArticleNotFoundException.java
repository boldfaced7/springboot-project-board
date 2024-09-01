package com.boldfaced7.board.common.exception.exception.article;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.entity.NotFoundException;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
