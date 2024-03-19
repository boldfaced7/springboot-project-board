package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.domain.Member;
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
    private Long memberId;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> attachmentNames;
    private List<String> attachmentUrls;
    private List<ArticleCommentDto> articleComments;

    public ArticleDto(Long articleId) {
        this.articleId = articleId;
    }

    public ArticleDto(Article article) {
        articleId = article.getId();
        memberId = article.getMember().getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getMember().getNickname();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
    }

    public ArticleDto(Article article, List<ArticleComment> articleComments) {
        articleId = article.getId();
        memberId = article.getMember().getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getMember().getNickname();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
        this.articleComments = articleComments.stream().map(ArticleCommentDto::new).toList();
    }

    public ArticleDto(Article article, List<ArticleComment> articleComments, List<String> attachmentUrls) {
        articleId = article.getId();
        memberId = article.getMember().getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getMember().getNickname();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
        this.attachmentUrls = attachmentUrls;
        this.articleComments = articleComments.stream().map(ArticleCommentDto::new).toList();
    }

    public Article toEntityForUpdating() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }

    public Article toEntityForSaving(Member member) {
        return Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}
