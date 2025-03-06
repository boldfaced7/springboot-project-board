package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticle;

import java.util.List;

public interface ListPagedArticlesQuery {
    List<ResolvedArticle> listArticles(ListAllArticlesCommand command);
}
