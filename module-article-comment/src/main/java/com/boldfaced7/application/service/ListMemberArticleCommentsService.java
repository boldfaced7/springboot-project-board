package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListMemberArticleCommentsCommand;
import com.boldfaced7.application.port.in.ListMemberArticleCommentsQuery;
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
public class ListMemberArticleCommentsService implements ListMemberArticleCommentsQuery {

    private static final int PAGE_SIZE = 20;

    private final GetMemberInfoPort getMemberInfoPort;
    private final ListMemberArticleCommentsPort listMemberArticleCommentsPort;

    @Override
    public List<ResolvedArticleComment> listMemberArticleComments(ListMemberArticleCommentsCommand command) {
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

    private List<ArticleComment> getArticleComments(ListMemberArticleCommentsCommand command) {
        ArticleComment.MemberId memberId = new ArticleComment.MemberId(command.memberId());
        int pageNumber = command.pageNumber();

        return listMemberArticleCommentsPort
                .listMemberArticleComments(memberId, pageNumber, PAGE_SIZE);
    }
}
