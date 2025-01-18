package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListArticlesCommand;
import com.boldfaced7.application.port.in.ListArticlesQuery;
import com.boldfaced7.application.port.out.ListArticlesPort;
import com.boldfaced7.application.port.out.ListMembersInfoPort;
import com.boldfaced7.application.port.out.ListMembersInfoRequest;
import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Query
@RequiredArgsConstructor
public class ListArticlesService implements ListArticlesQuery {

    private static final int PAGE_SIZE = 20;

    private final ListArticlesPort listArticlesPort;
    private final ListMembersInfoPort listMembersInfoPort;

    @Override
    public List<ResolvedArticle> listArticles(ListArticlesCommand command) {
        List<Article> fetched = getArticles(command.pageNumber());
        ListMembersInfoRequest membersRequest = createRequest(fetched);
        ListMembersInfoResponse membersResponse = listMembersInfoPort.getMembers(membersRequest);
        return ResolvedArticle.resolveAll(fetched, membersResponse.nicknames());
    }

    List<Article> getArticles(int pageNumber) {
        return listArticlesPort.listArticles(pageNumber, PAGE_SIZE);
    }

    ListMembersInfoRequest createRequest(List<Article> articles) {
        return articles.stream()
                .map(Article::getMemberId)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        ListMembersInfoRequest::new
                ));
    }
}
