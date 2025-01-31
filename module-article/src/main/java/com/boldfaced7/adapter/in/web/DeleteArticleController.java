package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.DeleteArticleCommand;
import com.boldfaced7.application.port.in.DeleteArticleUseCase;
import com.boldfaced7.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class DeleteArticleController {

    private final DeleteArticleUseCase deleteArticleUseCase;

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<DeleteArticleResponse> deleteArticle(
            @PathVariable String articleId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        Article deleted = delete(articleId, memberId);
        DeleteArticleResponse response = toResponse(deleted);
        return ResponseEntity.ok(response);
    }

    private Article delete(String articleId, String memberId) {
        DeleteArticleCommand command
                = new DeleteArticleCommand(articleId, memberId);
        return deleteArticleUseCase.deleteArticle(command);
    }

    private DeleteArticleResponse toResponse(Article deleted) {
        return new DeleteArticleResponse(
                deleted.getId(),
                deleted.getTitle(),
                deleted.getDeletedAt()
        );
    }

    public record DeleteArticleResponse(
            String articleId,
            String title,
            LocalDateTime deletedAt
    ) {}
}