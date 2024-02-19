package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ArticleCommentDto {
    private Long articleCommentId;
    private String content;
    private String author;
    private Long articleId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ArticleCommentDto(ArticleComment articleComment) {
        articleCommentId = articleComment.getId();
        content = articleComment.getContent();
        author = articleComment.getCreatedBy();
        articleId = articleComment.getArticle().getId();
        createdAt = articleComment.getCreatedAt();
        modifiedAt = articleComment.getModifiedAt();
    }

    public ArticleComment toEntity() {
        return ArticleComment.builder()
                .content(content)
                .build();
    }

    public ArticleComment toEntity(Article article) {
        return ArticleComment.builder()
                .content(content)
                .article(article)
                .build();
    }
}
