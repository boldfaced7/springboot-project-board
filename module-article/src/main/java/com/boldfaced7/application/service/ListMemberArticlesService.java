package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListMemberArticlesCommand;
import com.boldfaced7.application.port.in.ListMemberArticlesQuery;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.application.port.out.ListMemberArticlesPort;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListMemberArticlesService implements ListMemberArticlesQuery {

    private static final int PAGE_SIZE = 20;

    private final GetMemberInfoPort getMemberInfoPort;
    private final ListMemberArticlesPort listMemberArticlesPort;

    @Override
    public List<ResolvedArticle> listMemberArticles(ListMemberArticlesCommand command) {
        String nickname = getNickname(command.memberId());
        List<Article> articles = getArticles(command);
        return ResolvedArticle.resolveAll(articles, nickname);
    }

    private String getNickname(String memberId) {
        GetMemberInfoRequest request = new GetMemberInfoRequest(memberId);
        return getMemberInfoPort.getMember(request)
                .map(GetMemberInfoResponse::nickname)
                .orElseThrow(MemberNotFoundException::new);
    }

    private List<Article> getArticles(ListMemberArticlesCommand command) {
        Article.MemberId memberId = new Article.MemberId(command.memberId());
        int pageNumber = command.pageNumber();

        return listMemberArticlesPort
                .listMemberArticles(memberId, pageNumber, PAGE_SIZE);
    }
}
