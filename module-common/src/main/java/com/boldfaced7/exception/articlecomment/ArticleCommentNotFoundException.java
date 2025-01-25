package com.boldfaced7.exception.articlecomment;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.entity.NotFoundException;

public class ArticleCommentNotFoundException extends NotFoundException {
    public ArticleCommentNotFoundException() {
        super(ErrorCode.ARTICLE_COMMENT_NOT_FOUND);
    }
}
