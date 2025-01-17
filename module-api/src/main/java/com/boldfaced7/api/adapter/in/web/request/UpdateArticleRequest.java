package com.boldfaced7.api.adapter.in.web.request;

import com.boldfaced7.article.application.ArticleDto;
import com.boldfaced7.article.domain.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> attachmentNames = new ArrayList<>();

    public UpdateArticleRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ArticleDto toDto(Long targetId) {
        return ArticleDto.builder()
                .articleId(targetId)
                .title(title)
                .content(content)
                .attachmentNames(attachmentNames)
                .build();
    }
}
