package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.PostArticleCommentCommand;
import com.boldfaced7.application.port.in.PostArticleCommentUseCase;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class PostArticleCommentService implements PostArticleCommentUseCase {

    private final GetMemberInfoPort getMemberInfoPort;
    private final GetArticleInfoPort getArticleInfoPort;
    private final SaveArticleCommentPort saveArticleCommentPort;

    @Override
    public ArticleComment postArticleComment(PostArticleCommentCommand command) {
        ensureMemberExists(command.memberId());
        ensureArticleExists(command.articleId());
        return saveArticleComment(command);
    }

    private void ensureMemberExists(String memberId) {
        getMemberInfoPort.getMember(new GetMemberInfoRequest(memberId))
                .orElseThrow(MemberNotFoundException::new);
    }

    private void ensureArticleExists(String articleId) {
        GetArticleInfoRequest request = new GetArticleInfoRequest(articleId);
        getArticleInfoPort.findArticle(request)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private ArticleComment saveArticleComment(PostArticleCommentCommand command) {
        ArticleComment generated = ArticleComment.generate(
                new ArticleComment.ArticleId(command.articleId()),
                new ArticleComment.MemberId(command.memberId()),
                new ArticleComment.Content(command.content())
        );
        return saveArticleCommentPort.save(generated);
    }
}
