package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleDto;
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
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ArticleCommentResponse> articleCommentResponses;


    public ArticleResponse(ArticleDto dto) {
        this.articleId = dto.getArticleId();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.author = dto.getAuthor();
        this.createdAt = dto.getCreatedAt();
        this.modifiedAt = dto.getModifiedAt();
        this.articleCommentResponses = dto.getArticleComments()
                .stream().map(ArticleCommentResponse::new).toList();
    }
}
