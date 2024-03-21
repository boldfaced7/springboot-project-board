package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.AttachmentDto;
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
public class SaveArticleRequest {

    @NotBlank
    @Size(max = Article.MAX_TITLE_LENGTH)
    private String title;

    @NotBlank
    @Size(max = Article.MAX_CONTENT_LENGTH)
    private String content;

    private List<String> attachmentNames = new ArrayList<>();

    public SaveArticleRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .title(title)
                .content(content)
                .attachmentNames(attachmentNames)
                .build();
    }
}
