package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListArticlesByMemberCommand;
import com.boldfaced7.application.port.in.ListArticlesByMemberQuery;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.domain.Article.*;

@Slf4j
@Query
@RequiredArgsConstructor
public class ListArticlesByMemberService implements ListArticlesByMemberQuery {

    private static final int PAGE_SIZE = 20;

    private final ListArticleCachesByMemberPort listArticleCachesByMemberPort;
    private final ListArticlesByMemberPort listArticlesByMemberPort;
    private final ResolveArticleListPort resolveArticleListPort;

    @Override
    public List<ResolvedArticle> listMemberArticles(ListArticlesByMemberCommand command) {
        MemberId memberId = new MemberId(command.memberId());
        int pageNumber = command.pageNumber();

        List<ResolvedArticle> cached = listArticleCachesByMemberPort
                .listByMember(memberId, PAGE_SIZE, pageNumber);

        if (!cached.isEmpty()) {
            return cached;
        }
        return loadArticles(memberId, pageNumber);
    }

    private List<ResolvedArticle> loadArticles(MemberId memberId, int pageNumber) {
        List<Article> loaded = listArticlesByMemberPort
                .listByMember(memberId, PAGE_SIZE, pageNumber);
        return resolveArticleListPort.resolveArticles(loaded);
    }
}
