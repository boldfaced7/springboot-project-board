package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.PostArticleCommand;
import com.boldfaced7.application.port.in.PostArticleUseCase;
import com.boldfaced7.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class PostArticleController {

    private final PostArticleUseCase postArticleUseCase;

    @PostMapping("/articles")
    public ResponseEntity<PostArticleResponse> postArticle(
            @RequestBody PostArticleRequest request,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        Article posted = post(memberId, request.title(), request.content());
        PostArticleResponse response = toResponse(posted);
        return ResponseEntity.ok(response);
    }

    private Article post(String memberId, String title, String content) {
        PostArticleCommand command
                = new PostArticleCommand(memberId, title, content);
        return postArticleUseCase.postArticle(command);
    }

    private PostArticleResponse toResponse(Article article) {
        return new PostArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getCreatedAt()
        );
    }

    public record PostArticleRequest(
            String title,
            String content
    ) {}

    public record PostArticleResponse(
            String articleId,
            String title,
            LocalDateTime createdAt
    ) {}
}
