package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.UpdateArticleCommentCommand;
import com.boldfaced7.application.port.in.UpdateArticleCommentUseCase;
import com.boldfaced7.application.port.out.FindArticleCommentPort;
import com.boldfaced7.application.port.out.UpdateArticleCommentPort;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UpdateArticleCommentService implements UpdateArticleCommentUseCase {

    private final FindArticleCommentPort findArticleCommentPort;
    private final UpdateArticleCommentPort updateArticleCommentPort;

    @Override
    public ArticleComment updateArticleComment(UpdateArticleCommentCommand command) {
        ArticleComment found = getArticleComment(command.articleCommentId());
        ensureAuthentication(command.memberId(), found.getMemberId());
        return updateArticleComment(found, command.content());
    }

    private ArticleComment getArticleComment(String articleId) {
        ArticleComment.Id id = new ArticleComment.Id(articleId);
        return findArticleCommentPort.findById(id)
                .orElseThrow(ArticleCommentNotFoundException::new);
    }

    private void ensureAuthentication(String fromCommand, String found) {
        if (!found.equals(fromCommand)) {
            throw new UnauthorizedException();
        }
    }

    private ArticleComment updateArticleComment(ArticleComment articleComment, String newContent) {
        ArticleComment source = articleComment.update(
                new ArticleComment.Content(newContent)
        );
        return updateArticleCommentPort.update(source);
    }
}
