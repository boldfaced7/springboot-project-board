package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListAllArticlesCommand;
import com.boldfaced7.application.port.in.ListPagedArticlesQuery;
import com.boldfaced7.domain.ResolvedArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequestMapping("/api")
@RequiredArgsConstructor
public class ListPagedArticlesController {

    private final ListPagedArticlesQuery listPagedArticlesQuery;

    @GetMapping("/articles")
    public ResponseEntity<List<ListArticlesResponse>> listAllArticles(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedArticle> articles = listAll(page);
        List<ListArticlesResponse> response = toResponse(articles);
        return ResponseEntity.ok(response);
    }

    private List<ResolvedArticle> listAll(int page) {
        ListAllArticlesCommand command = new ListAllArticlesCommand(page);
        return listPagedArticlesQuery.listArticles(command);
    }

    private List<ListArticlesResponse> toResponse(List<ResolvedArticle> articles) {
        return articles.stream()
                .map(article -> new ListArticlesResponse(
                        article.getId(),
                        article.getTitle(),
                        article.getNickname(),
                        article.getCreatedAt()
                ))
                .toList();
    }

    public record ListArticlesResponse(
            String articleId,
            String title,
            String nickname,
            LocalDateTime createdAt
    ) {}
}
