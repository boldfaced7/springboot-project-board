package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Article;

public interface PostArticleUseCase {
    Article postArticle(PostArticleCommand command);
}
