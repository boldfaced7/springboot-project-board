package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;

public interface SaveArticlePort {
    Article save(Article article);
}
