package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.ArticleDto;
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
public class UpdateArticleRequest {

    @NotBlank
    @Size(max = Article.MAX_TITLE_LENGTH)
    private String title;

    @NotBlank
    @Size(max = Article.MAX_CONTENT_LENGTH)
    private String content;

    public ArticleDto toDto(Long targetId) {
        return ArticleDto.builder()
                .articleId(targetId)
                .title(title)
                .content(content)
                .build();
    }
}
