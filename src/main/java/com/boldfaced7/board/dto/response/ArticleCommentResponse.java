package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentResponse {

    private Long articleCommentId;
    private String content;
    private String author;
    private Long articleId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ArticleCommentResponse(ArticleCommentDto dto) {
        this.articleCommentId = dto.getArticleCommentId();
        this.content = dto.getContent();
        this.author = dto.getAuthor();
        this.articleId = dto.getArticleId();
        this.createdAt = dto.getCreatedAt();
        this.modifiedAt = dto.getModifiedAt();
    }
}
