package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleComment;

import java.util.Optional;

public interface FindArticleCommentPort {
    Optional<ArticleComment> findById(ArticleComment.Id id);
}
