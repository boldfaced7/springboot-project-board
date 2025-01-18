package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticle;

public interface GetArticleQuery {
    ResolvedArticle getArticle(GetArticleCommand command);
}
