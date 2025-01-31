package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.GetArticleCommand;
import com.boldfaced7.application.port.in.GetArticleQuery;
import com.boldfaced7.domain.ResolvedArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class GetArticleController {

    private final GetArticleQuery getArticleQuery;

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<GetArticleResponse> getArticle(
            @PathVariable String articleId
    ) {
        ResolvedArticle article = get(articleId);
        GetArticleResponse response = toResponse(article);
        return ResponseEntity.ok(response);
    }

    private ResolvedArticle get(String articleId) {
        GetArticleCommand command = new GetArticleCommand(articleId);
        return getArticleQuery.getArticle(command);
    }

    public GetArticleResponse toResponse(ResolvedArticle article) {
        return new GetArticleResponse(
                article.getId(),
                article.getMemberId(),
                article.getEmail(),
                article.getNickname(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.isUpdated(),
                article.getAttachmentUrls()
        );
    }

    public record GetArticleResponse(
            String articleId,
            String memberId,
            String email,
            String nickname,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            boolean isUpdated,
            List<String> attachmentUrls
    ) {}
}
