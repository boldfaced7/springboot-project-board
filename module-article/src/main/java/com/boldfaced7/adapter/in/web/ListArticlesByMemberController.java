package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListArticlesByMemberCommand;
import com.boldfaced7.application.port.in.ListArticlesByMemberQuery;
import com.boldfaced7.domain.ResolvedArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListArticlesByMemberController {

    private final ListArticlesByMemberQuery listArticlesByMemberQuery;

    @GetMapping("/members/{memberId}/articles")
    public ResponseEntity<List<ListMemberArticlesResponse>> listMemberArticles(
            @PathVariable String memberId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedArticle> articles = getResolvedArticles(memberId, page);
        List<ListMemberArticlesResponse> responses = toResponse(articles);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedArticle> getResolvedArticles(String memberId, int page) {
        ListArticlesByMemberCommand command = new ListArticlesByMemberCommand(memberId, page);
        return listArticlesByMemberQuery.listMemberArticles(command);
    }

    private List<ListMemberArticlesResponse> toResponse(List<ResolvedArticle> articles) {
        return articles.stream()
                .map(article -> new ListMemberArticlesResponse(
                        article.getId(),
                        article.getTitle(),
                        article.getNickname(),
                        article.getCreatedAt()
                ))
                .toList();
    }

    public record ListMemberArticlesResponse(
            String articleId,
            String title,
            String nickname,
            LocalDateTime createdAt
    ) {}
}
