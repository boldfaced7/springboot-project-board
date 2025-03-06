package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;

import java.util.Optional;

public interface FindArticlePort {
    Optional<Article> findById(Article.Id id);
}
