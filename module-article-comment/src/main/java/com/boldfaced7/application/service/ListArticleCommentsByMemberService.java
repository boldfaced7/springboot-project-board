package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListArticleCommentsByMemberCommand;
import com.boldfaced7.application.port.in.ListArticleCommentsByMemberQuery;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.application.port.out.ListMemberArticleCommentsPort;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.domain.ResolvedArticleComment;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListArticleCommentsByMemberService implements ListArticleCommentsByMemberQuery {

    private static final int PAGE_SIZE = 20;

    private final GetMemberInfoPort getMemberInfoPort;
    private final ListMemberArticleCommentsPort listMemberArticleCommentsPort;

    @Override
    public List<ResolvedArticleComment> listArticleComments(ListArticleCommentsByMemberCommand command) {
        String nickname = getNickname(command.memberId());
        List<ArticleComment> articleComments = getArticleComments(command);
        return ResolvedArticleComment.resolveAll(articleComments, nickname);
    }

    private String getNickname(String memberId) {
        GetMemberInfoRequest request = new GetMemberInfoRequest(memberId);
        return getMemberInfoPort.getMember(request)
                .map(GetMemberInfoResponse::nickname)
                .orElseThrow(MemberNotFoundException::new);
    }

    private List<ArticleComment> getArticleComments(ListArticleCommentsByMemberCommand command) {
        ArticleComment.MemberId memberId = new ArticleComment.MemberId(command.memberId());
        int pageNumber = command.pageNumber();

        return listMemberArticleCommentsPort
                .listMemberArticleComments(memberId, pageNumber, PAGE_SIZE);
    }
}
