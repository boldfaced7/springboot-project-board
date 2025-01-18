package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleComment;

public interface GetArticleCommentQuery {
    ResolvedArticleComment getArticleComment(GetArticleCommentCommand command);
}
