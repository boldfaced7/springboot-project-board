package com.boldfaced7.board.comment.presentation.request;

import com.boldfaced7.board.comment.domain.ArticleComment;
import com.boldfaced7.board.comment.application.ArticleCommentDto;
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
public class UpdateArticleCommentRequest {

    @NotBlank
    @Size(max = ArticleComment.MAX_CONTENT_LENGTH)
    private String content;

    public ArticleCommentDto toDto(Long targetId) {
        return ArticleCommentDto.builder()
                .articleCommentId(targetId)
                .content(content)
                .build();
    }
}