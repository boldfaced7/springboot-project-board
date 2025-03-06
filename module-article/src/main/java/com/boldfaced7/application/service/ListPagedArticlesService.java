package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAllArticlesCommand;
import com.boldfaced7.application.port.in.ListPagedArticlesQuery;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListPagedArticlesService implements ListPagedArticlesQuery {

    private static final int PAGE_SIZE = 20;

    private final ListPagedArticleCachesPort listPagedArticleCachesPort;
    private final ListPagedArticlesPort listPagedArticlesPort;
    private final ResolveArticleListPort resolveArticleListPort;

    @Override
    public List<ResolvedArticle> listArticles(ListAllArticlesCommand command) {
        return listPagedArticleCachesPort.listArticles(command.pageNumber(), PAGE_SIZE)
                .orElseGet(() -> loadArticles(command.pageNumber()));
    }

    public List<ResolvedArticle> loadArticles(int pageNumber) {
        List<Article> fetched = listPagedArticlesPort.listArticles(pageNumber, PAGE_SIZE);
        return resolveArticleListPort.resolveArticles(fetched);
    }
}
