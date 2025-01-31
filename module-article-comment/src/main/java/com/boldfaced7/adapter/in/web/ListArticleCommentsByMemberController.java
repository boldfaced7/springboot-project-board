package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListArticleCommentsByMemberCommand;
import com.boldfaced7.application.port.in.ListArticleCommentsByMemberQuery;
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
public class ListArticleCommentsByMemberController {

    private final ListArticleCommentsByMemberQuery listArticleCommentsByMemberQuery;

    @GetMapping("/members/{memberId}/comments")
    public ResponseEntity<List<ListArticleCommentsByMemberResponse>> listArticleComments(
            @PathVariable String memberId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedArticleComment> comments = getComments(memberId, page);
        List<ListArticleCommentsByMemberResponse> responses = toResponses(comments);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedArticleComment> getComments(String memberId, int page) {
        ListArticleCommentsByMemberCommand command
                = new ListArticleCommentsByMemberCommand(memberId, page);
        return listArticleCommentsByMemberQuery.listArticleComments(command);
    }

    private static List<ListArticleCommentsByMemberResponse> toResponses(
            List<ResolvedArticleComment> comments
    ) {
        return comments.stream()
                .map(comment -> new ListArticleCommentsByMemberResponse(
                        comment.getId(),
                        comment.getArticleId(),
                        comment.getMemberId(),
                        comment.getContent(),
                        comment.getNickname(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

    public record ListArticleCommentsByMemberResponse(
            String id,
            String articleId,
            String memberId,
            String content,
            String nickname,
            LocalDateTime createdAt
    ) {}
}