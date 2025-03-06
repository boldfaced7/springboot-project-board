package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleComment;

import java.util.List;

public interface ListAllArticleCommentsPort {
    List<ArticleComment> listAll(int pageNumber, int pageSize);
}
