package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ResolvedArticle;

import java.util.List;
import java.util.Optional;

public interface ListPagedArticleCachesPort {
    Optional<List<ResolvedArticle>> listArticles(int pageNumber, int pageSize);
}
