package com.boldfaced7.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResolvedArticleComment {

    private final ArticleComment articleComment;
    @Getter private final String nickname;
    @Getter private final boolean updated;

    public static ResolvedArticleComment resolve(
            ArticleComment articleComment,
            String nickname
    ) {
        return new ResolvedArticleComment(
                articleComment,
                nickname
        );
    }

    public static List<ResolvedArticleComment> resolveAll(
            List<ArticleComment> articleComments,
            List<String> nicknames
    ) {
        List<ResolvedArticleComment> resolvedArticleComments = new ArrayList<>();

        for (int i = 0; i < articleComments.size(); i++) {
            ArticleComment articleComment = articleComments.get(i);
            String nickname = nicknames.get(i);
            resolvedArticleComments.add(new ResolvedArticleComment(articleComment, nickname));
        }
        return resolvedArticleComments;
    }

    public static List<ResolvedArticleComment> resolveAll(
            List<ArticleComment> ArticleComments,
            String nickname
    ) {
        return ArticleComments.stream()
                .map(comment -> new ResolvedArticleComment(comment, nickname))
                .toList();
    }

    private ResolvedArticleComment(
            ArticleComment articleComment,
            String nickname
    ) {
        this.articleComment = articleComment;
        this.nickname = nickname;
        this.updated = !articleComment.getUpdatedAt()
                .equals(articleComment.getCreatedAt());
    }

    public String getId() {
        return articleComment.getId();
    }

    public String getArticleId() {
        return articleComment.getArticleId();
    }

    public String getMemberId() {
        return articleComment.getMemberId();
    }

    public String getContent() {
        return articleComment.getContent();
    }

    public LocalDateTime getCreatedAt() {
        return articleComment.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return articleComment.getUpdatedAt();
    }

    public LocalDateTime getDeletedAt() {
        return articleComment.getDeletedAt();
    }
}
