package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ArticleComment;

public interface PostArticleCommentUseCase {
    ArticleComment postArticleComment(PostArticleCommentCommand command);
}
