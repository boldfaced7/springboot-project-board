package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;

public interface DeleteArticlePort {
    Article delete(Article article);
}
