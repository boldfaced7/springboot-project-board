package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;

public interface ResolveArticlePort {
    ResolvedArticle resolveArticle(Article article);
}
