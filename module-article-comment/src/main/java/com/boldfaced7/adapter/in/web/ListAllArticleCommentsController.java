package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListAllArticleCommentsCommand;
import com.boldfaced7.application.port.in.ListAllArticleCommentsQuery;
import com.boldfaced7.domain.ResolvedArticleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListAllArticleCommentsController {

    private final ListAllArticleCommentsQuery listAllArticleCommentsQuery;

    @GetMapping("/comments")
    public ResponseEntity<List<ListAllArticleCommentsResponse>> listAllArticleComments(
            @RequestParam Integer pageNumber) {
        List<ResolvedArticleComment> comments = getComments(pageNumber);
        List<ListAllArticleCommentsResponse> responses = toResponses(comments);
        return ResponseEntity.ok(responses);
    }

    private static List<ListAllArticleCommentsResponse> toResponses(List<ResolvedArticleComment> comments) {
        return comments.stream()
                .map(comment -> new ListAllArticleCommentsResponse(
                        comment.getId(),
                        comment.getArticleId(),
                        comment.getMemberId(),
                        comment.getContent(),
                        comment.getNickname(),
                        comment.getCreatedAt().toString()
                ))
                .toList();
    }

    private List<ResolvedArticleComment> getComments(Integer pageNumber) {
        ListAllArticleCommentsCommand command = new ListAllArticleCommentsCommand(pageNumber);
        return listAllArticleCommentsQuery.listAllArticleComments(command);
    }

    public record ListAllArticleCommentsResponse(
            String id,
            String articleId,
            String memberId,
            String content,
            String nickname,
            String createdAt
    ) {}
}
