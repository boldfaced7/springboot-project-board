package com.boldfaced7.common.exception.exception.exception.articlecomment;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.entity.NotFoundException;

public class ArticleCommentNotFoundException extends NotFoundException {
    public ArticleCommentNotFoundException() {
        super(ErrorCode.ARTICLE_COMMENT_NOT_FOUND);
    }
}
