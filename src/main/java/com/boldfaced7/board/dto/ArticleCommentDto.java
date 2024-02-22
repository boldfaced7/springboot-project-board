package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.response.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ArticleCommentDto {
    private Long articleCommentId;
    private Long articleId;
    private Long memberId;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ArticleCommentDto(ArticleComment articleComment) {
        articleCommentId = articleComment.getId();
        articleId = articleComment.getArticle().getId();
        memberId = articleComment.getMember().getId();
        content = articleComment.getContent();
        author = articleComment.getMember().getNickname();
        createdAt = articleComment.getCreatedAt();
        modifiedAt = articleComment.getModifiedAt();
    }

    public ArticleComment toEntityForUpdating() {
        return ArticleComment.builder()
                .content(content)
                .build();
    }

    public ArticleComment toEntityForSaving(Article article, Member member) {
        return ArticleComment.builder()
                .content(content)
                .article(article)
                .member(member)
                .build();
    }
}
