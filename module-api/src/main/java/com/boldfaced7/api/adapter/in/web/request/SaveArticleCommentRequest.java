package com.boldfaced7.api.adapter.in.web.request;

import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.application.ArticleCommentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveArticleCommentRequest {

    @NotBlank
    @Size(max = ArticleComment.MAX_CONTENT_LENGTH)
    private String content;

    public ArticleCommentDto toDto(Long articleId) {
        return ArticleCommentDto.builder()
                .articleId(articleId)
                .content(content)
                .build();
    }
}
