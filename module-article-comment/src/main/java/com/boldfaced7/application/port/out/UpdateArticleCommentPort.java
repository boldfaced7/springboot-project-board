package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleComment;

public interface UpdateArticleCommentPort {
    ArticleComment update(ArticleComment articleComment);
}
