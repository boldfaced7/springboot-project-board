package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleComment;

import java.util.List;

public interface ListArticleCommentsPort {
    List<ArticleComment> listArticleComments(ArticleComment.ArticleId ArticleId, int pageNumber, int pageSize);
}
