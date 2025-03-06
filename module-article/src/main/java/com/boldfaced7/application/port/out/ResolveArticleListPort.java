package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;

import java.util.List;

public interface ResolveArticleListPort {
    List<ResolvedArticle> resolveArticles(List<Article> articles);
}
