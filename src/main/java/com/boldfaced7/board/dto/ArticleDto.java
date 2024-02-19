package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ArticleDto {

    private Long articleId;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ArticleCommentDto> articleComments;

    public ArticleDto(Article article) {
        articleId = article.getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getCreatedBy();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
    }

    public ArticleDto(Article article, List<ArticleComment> articleComments) {
        articleId = article.getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getCreatedBy();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
        this.articleComments = articleComments.stream().map(ArticleCommentDto::new).toList();
    }

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
