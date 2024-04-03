package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long articleId;
    private Long memberId;
    private String title;
    private String content;
    private String author;
    private Pageable pageable;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> attachmentNames = new ArrayList<>();
    private List<String> attachmentUrls = new ArrayList<>();
    private CustomPage<ArticleCommentDto> articleComments = CustomPage.empty();

    public ArticleDto(Long articleId, Pageable pageable) {
        this.articleId = articleId;
        this.pageable = pageable;
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

    public ArticleDto(Article article, Member member) {
        articleId = article.getId();
        memberId = member.getId();
        title = article.getTitle();
        content = article.getContent();
        author = member.getNickname();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
    }

    public ArticleDto(Article article, CustomPage<ArticleCommentDto> articleComments, List<String> attachmentUrls) {
        articleId = article.getId();
        memberId = article.getMember().getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getMember().getNickname();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
        this.attachmentUrls = attachmentUrls;
        this.articleComments = articleComments;
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
