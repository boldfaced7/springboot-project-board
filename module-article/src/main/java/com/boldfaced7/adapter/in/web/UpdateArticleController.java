package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.UpdateArticleCommand;
import com.boldfaced7.application.port.in.UpdateArticleUseCase;
import com.boldfaced7.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class UpdateArticleController {

    private final UpdateArticleUseCase updateArticleUseCase;

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<UpdateArticleResponse> updateArticle(
            @RequestBody UpdateArticleRequest request,
            @PathVariable String articleId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        Article updated = update(articleId, memberId, request.title(), request.content());
        UpdateArticleResponse response = toResponse(updated);
        return ResponseEntity.ok(response);
    }

    private Article update(String articleId, String memberId, String title, String content) {
        UpdateArticleCommand command = new UpdateArticleCommand(
                articleId, memberId, title, content);
        return updateArticleUseCase.updateArticle(command);
    }

    public UpdateArticleResponse toResponse(Article article) {
        return new UpdateArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getCreatedAt()
        );
    }

    public record UpdateArticleRequest(
            String title,
            String content
    ) {}

    public record UpdateArticleResponse(
            String articleId,
            String title,
            LocalDateTime createdAt
    ) {}
}
