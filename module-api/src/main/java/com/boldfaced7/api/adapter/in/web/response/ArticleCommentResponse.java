package com.boldfaced7.api.adapter.in.web.response;

import com.boldfaced7.application.ArticleCommentDto;
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
    private Long articleId;
    private Long memberId;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ArticleCommentResponse(ArticleCommentDto dto) {
        articleCommentId = dto.getArticleCommentId();
        memberId = dto.getMemberId();
        content = dto.getContent();
        author = dto.getAuthor();
        articleId = dto.getArticleId();
        createdAt = dto.getCreatedAt();
        modifiedAt = dto.getModifiedAt();
    }
}
