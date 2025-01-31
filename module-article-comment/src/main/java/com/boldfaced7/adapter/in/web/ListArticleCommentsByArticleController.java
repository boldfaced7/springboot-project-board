package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListArticleCommentsByArticleCommand;
import com.boldfaced7.application.port.in.ListArticleCommentsByArticleQuery;
import com.boldfaced7.domain.ResolvedArticleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListArticleCommentsByArticleController {

    private final ListArticleCommentsByArticleQuery listArticleCommentsByArticleQuery;

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<ListArticleCommentsByArticleResponse>> listArticleComments(
            @PathVariable String articleId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedArticleComment> comments = getComments(articleId, page);
        List<ListArticleCommentsByArticleResponse> responses = toResponses(comments);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedArticleComment> getComments(String articleId, int page) {
        ListArticleCommentsByArticleCommand command
                = new ListArticleCommentsByArticleCommand(articleId, page);
        return listArticleCommentsByArticleQuery.listArticleComments(command);
    }

    private static List<ListArticleCommentsByArticleResponse> toResponses(
            List<ResolvedArticleComment> comments
    ) {
        return comments.stream()
                .map(comment -> new ListArticleCommentsByArticleResponse(
                        comment.getId(),
                        comment.getArticleId(),
                        comment.getMemberId(),
                        comment.getContent(),
                        comment.getNickname(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

    public record ListArticleCommentsByArticleResponse(
            String id,
            String articleId,
            String memberId,
            String content,
            String nickname,
            LocalDateTime createdAt
    ) {}
}