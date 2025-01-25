package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Article;

public interface DeleteArticleUseCase {
    Article deleteArticle(DeleteArticleCommand command);
}
