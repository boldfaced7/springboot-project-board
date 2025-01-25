package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteArticleCommand;
import com.boldfaced7.application.port.in.DeleteArticleUseCase;
import com.boldfaced7.application.port.out.FindArticlePort;
import com.boldfaced7.application.port.out.UpdateArticlePort;
import com.boldfaced7.domain.Article;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteArticleService implements DeleteArticleUseCase {

    private final FindArticlePort findArticlePort;
    private final UpdateArticlePort updateArticlePort;

    @Override
    public Article deleteArticle(DeleteArticleCommand command) {
        Article found = getArticle(command.articleId());
        ensureMemberAuth(command.memberId(), found.getMemberId());
        Article deleted = found.delete();
        return updateArticlePort.update(deleted);
    }

    private Article getArticle(String articleId) {
        Article.Id id = new Article.Id(articleId);
        return findArticlePort.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private void ensureMemberAuth(String fromCommand, String found) {
        if (!found.equals(fromCommand)) {
            throw new UnauthorizedException();
        }
    }
}
