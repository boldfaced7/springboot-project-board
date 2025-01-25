package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.GetArticleCommentCommand;
import com.boldfaced7.application.port.in.GetArticleCommentQuery;
import com.boldfaced7.application.port.out.FindArticleCommentPort;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.domain.ResolvedArticleComment;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

@Query
@RequiredArgsConstructor
public class GetArticleCommentService implements GetArticleCommentQuery {

    private final FindArticleCommentPort findArticleCommentPort;
    private final GetMemberInfoPort getMemberInfoPort;

    @Override
    public ResolvedArticleComment getArticleComment(GetArticleCommentCommand command) {
        ArticleComment found = getArticleComment(command.articleCommentId());
        String string = getNickname(found.getMemberId());
        return ResolvedArticleComment.resolve(found, string);
    }

    private ArticleComment getArticleComment(String articleId) {
        ArticleComment.Id id = new ArticleComment.Id(articleId);
        return findArticleCommentPort.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private String getNickname(String memberId) {
        GetMemberInfoRequest request = new GetMemberInfoRequest(memberId);
        return getMemberInfoPort.getMember(request)
                .map(GetMemberInfoResponse::nickname)
                .orElseThrow(MemberNotFoundException::new);
    }
}
