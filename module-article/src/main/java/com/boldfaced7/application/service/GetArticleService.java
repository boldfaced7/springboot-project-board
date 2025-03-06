package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.GetArticleCommand;
import com.boldfaced7.application.port.in.GetArticleQuery;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.boldfaced7.domain.Article.*;

@Query
@RequiredArgsConstructor
public class GetArticleService implements GetArticleQuery {

    private final FindArticleCachePort findArticleCachePort;
    private final FindArticlePort findArticlePort;
    private final ResolveArticlePort resolveArticlePort;

    @Override
    public ResolvedArticle getArticle(GetArticleCommand command) {
        Id id = new Id(command.articleId());
        return findArticleCachePort.findById(id)
                .orElseGet(() -> loadArticle(id));
    }

    public ResolvedArticle loadArticle(Id id) {
        Article found = findArticlePort.findById(id)
                .orElseThrow(ArticleNotFoundException::new);

        return resolveArticlePort.resolveArticle(found);
    }
}
