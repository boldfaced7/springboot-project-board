package com.boldfaced7.board.comment.application;

import com.boldfaced7.board.article.domain.Article;
import com.boldfaced7.board.comment.domain.ArticleComment;
import com.boldfaced7.board.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentDto {
    private Long articleCommentId;
    private Long articleId;
    private Long memberId;
    private String content;
    private String author;
    private Pageable pageable;
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

    public ArticleCommentDto(ArticleComment articleComment, Article article) {
        articleCommentId = articleComment.getId();
        articleId = article.getId();
        memberId = articleComment.getMember().getId();
        content = articleComment.getContent();
        author = articleComment.getMember().getNickname();
        createdAt = articleComment.getCreatedAt();
        modifiedAt = articleComment.getModifiedAt();
    }

    public ArticleCommentDto(ArticleComment articleComment, Member member) {
        articleCommentId = articleComment.getId();
        articleId = articleComment.getArticle().getId();
        memberId = member.getId();
        content = articleComment.getContent();
        author = member.getNickname();
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
