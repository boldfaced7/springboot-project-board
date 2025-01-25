package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ArticleComment;

public interface DeleteArticleCommentUseCase {
    ArticleComment deleteArticleComment(DeleteArticleCommentCommand command);
}
