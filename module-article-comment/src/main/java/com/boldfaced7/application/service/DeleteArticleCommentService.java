package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteArticleCommentCommand;
import com.boldfaced7.application.port.in.DeleteArticleCommentUseCase;
import com.boldfaced7.application.port.out.FindArticleCommentPort;
import com.boldfaced7.application.port.out.UpdateArticleCommentPort;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteArticleCommentService implements DeleteArticleCommentUseCase {

    private final FindArticleCommentPort findArticleCommentPort;
    private final UpdateArticleCommentPort updateArticleCommentPort;

    @Override
    public ArticleComment deleteArticleComment(DeleteArticleCommentCommand command) {
        ArticleComment found = getArticleComment(command.targetArticleCommentId());
        ensureAuthentication(command.requiringMemberId(), found.getMemberId());
        ArticleComment deleted = found.delete();
        return updateArticleCommentPort.update(deleted);
    }

    private ArticleComment getArticleComment(String articleCommentId) {
        ArticleComment.Id id = new ArticleComment.Id(articleCommentId);
        return findArticleCommentPort.findById(id)
                .orElseThrow(ArticleCommentNotFoundException::new);
    }

    private void ensureAuthentication(String fromCommand, String found) {
        if (!found.equals(fromCommand)) {
            throw new UnauthorizedException();
        }
    }
}
