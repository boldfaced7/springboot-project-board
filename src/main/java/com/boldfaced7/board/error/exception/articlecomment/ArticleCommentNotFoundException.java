package com.boldfaced7.board.error.exception.articlecomment;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.entity.NotFoundException;

public class ArticleCommentNotFoundException extends NotFoundException {
    public ArticleCommentNotFoundException() {
        super(ErrorCode.ARTICLE_COMMENT_NOT_FOUND);
    }
}
