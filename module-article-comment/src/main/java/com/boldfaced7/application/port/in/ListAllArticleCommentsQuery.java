package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleComment;

import java.util.List;

public interface ListAllArticleCommentsQuery {
    List<ResolvedArticleComment> listAllArticleComments(ListAllArticleCommentsCommand command);
}
