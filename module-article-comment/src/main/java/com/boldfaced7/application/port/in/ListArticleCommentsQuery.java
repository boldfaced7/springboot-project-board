package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleComment;

import java.util.List;

public interface ListArticleCommentsQuery {
    List<ResolvedArticleComment> listArticleComments(ListArticleCommentsCommand command);
}
