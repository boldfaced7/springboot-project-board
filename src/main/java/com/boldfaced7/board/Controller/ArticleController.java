package com.boldfaced7.board.Controller;

import com.boldfaced7.board.Service.ArticleService;
import com.boldfaced7.board.dto.request.ArticleRequest;
import com.boldfaced7.board.dto.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/v1/articles")
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final String urlTemplate = "/api/v1/articles";

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getArticles() {
        List<ArticleResponse> articles = articleService.getArticles()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long articleId) {
        ArticleResponse article = articleService.getArticle(articleId).toResponse();

        return ResponseEntity.ok()
                .body(article);
    }

    @PostMapping
    private ResponseEntity<Void> postNewArticle(@RequestBody ArticleRequest articleRequest) {
        Long articleId = articleService.saveArticle(articleRequest.toDto());

        return ResponseEntity.created(URI.create(urlTemplate + "/" + articleId)).build();
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long articleId, @RequestBody ArticleRequest articleRequest) {
        articleService.updateArticle(articleId, articleRequest.toDto());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.softDeleteArticle(articleId);

        return ResponseEntity.ok().build();
    }
}
