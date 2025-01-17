package com.boldfaced7.api.adapter.in.web.response;

import com.boldfaced7.article.application.ArticleDto;
import com.boldfaced7.common.CustomPage;
import com.boldfaced7.comment.presentation.response.ArticleCommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponse {
    private Long articleId;
    private Long memberId;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> attachmentUrls;
    private CustomPage<ArticleCommentResponse> articleComments;


    public ArticleResponse(ArticleDto dto) {
        articleId = dto.getArticleId();
        memberId = dto.getMemberId();
        title = dto.getTitle();
        content = dto.getContent();
        author = dto.getAuthor();
        createdAt = dto.getCreatedAt();
        modifiedAt = dto.getModifiedAt();
        attachmentUrls = dto.getAttachmentUrls();
        articleComments = dto.getArticleComments().map(ArticleCommentResponse::new);
    }
}
