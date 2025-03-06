package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;

import java.util.List;

public interface ListPagedArticlesPort {
    List<Article> listArticles(int pageNumber, int pageSize);
}
