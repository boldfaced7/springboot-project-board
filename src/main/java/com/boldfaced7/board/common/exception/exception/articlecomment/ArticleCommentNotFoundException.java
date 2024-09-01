package com.boldfaced7.board.common.exception.exception.articlecomment;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.entity.NotFoundException;

public class ArticleCommentNotFoundException extends NotFoundException {
    public ArticleCommentNotFoundException() {
        super(ErrorCode.ARTICLE_COMMENT_NOT_FOUND);
    }
}
