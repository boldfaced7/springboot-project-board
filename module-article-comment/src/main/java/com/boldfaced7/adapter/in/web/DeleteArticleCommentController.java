package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.DeleteArticleCommentCommand;
import com.boldfaced7.application.port.in.DeleteArticleCommentUseCase;
import com.boldfaced7.domain.ArticleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class DeleteArticleCommentController {

    private final DeleteArticleCommentUseCase deleteArticleCommentUseCase;

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<DeleteArticleCommentResponse> deleteArticleComment(
            @PathVariable String commentId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleComment deleted = deleteComment(commentId, memberId);
        DeleteArticleCommentResponse response = toResponse(deleted);
        return ResponseEntity.ok(response);
    }

    private ArticleComment deleteComment(String commentId, String memberId) {
        DeleteArticleCommentCommand command = new DeleteArticleCommentCommand(commentId, memberId);
        return deleteArticleCommentUseCase.deleteArticleComment(command);
    }

    private DeleteArticleCommentResponse toResponse(ArticleComment articleComment) {
        return new DeleteArticleCommentResponse(
                articleComment.getId(),
                articleComment.getDeletedAt()
        );
    }

    public record DeleteArticleCommentResponse(
            String id,
            LocalDateTime deletedAt
    ) {}
}