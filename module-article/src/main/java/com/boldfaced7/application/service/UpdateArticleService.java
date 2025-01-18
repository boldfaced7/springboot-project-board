package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.UpdateArticleCommand;
import com.boldfaced7.application.port.in.UpdateArticleUseCase;
import com.boldfaced7.application.port.out.FindArticlePort;
import com.boldfaced7.application.port.out.UpdateArticlePort;
import com.boldfaced7.domain.Article;
import com.boldfaced7.UseCase;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UpdateArticleService implements UpdateArticleUseCase {

    private final FindArticlePort findArticlePort;
    private final UpdateArticlePort updateArticlePort;

    @Override
    public Article updateArticle(UpdateArticleCommand command) {
        Article found = getArticle(command.articleId());
        ensureMemberAuth(command.memberId(), found.getMemberId());
        return updateArticle(found, command.title(), command.content());
    }

    private Article getArticle(String articleId) {
        Article.Id id = new Article.Id(articleId);
        return findArticlePort.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private Article updateArticle(Article article, String newTitle, String newContent) {
        Article source = article.update(
                new Article.Title(newTitle),
                new Article.Content(newContent)
        );
        return updateArticlePort.update(source);
    }

    private void ensureMemberAuth(String fromCommand, String found) {
        if (!found.equals(fromCommand)) {
            throw new UnauthorizedException();
        }
    }


}
