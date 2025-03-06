package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;

import java.util.Optional;

public interface FindArticleCachePort {
    Optional<ResolvedArticle> findById(Article.Id id);
}
