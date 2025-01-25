package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAllArticleCommentsCommand;
import com.boldfaced7.application.port.in.ListAllArticleCommentsQuery;
import com.boldfaced7.application.port.out.ListAllArticleCommentsPort;
import com.boldfaced7.application.port.out.ListMembersInfoPort;
import com.boldfaced7.application.port.out.ListMembersInfoRequest;
import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.domain.ResolvedArticleComment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListAllArticleCommentsService implements ListAllArticleCommentsQuery {

    private static final int PAGE_SIZE = 20;

    private final ListAllArticleCommentsPort listAllArticleCommentsPort;
    private final ListMembersInfoPort listMembersInfoPort;

    @Override
    public List<ResolvedArticleComment> listAllArticleComments(ListAllArticleCommentsCommand command) {
        List<ArticleComment> fetched = getArticleComments(command.pageNumber());
        List<String> nicknames = getNicknames(fetched);
        return ResolvedArticleComment.resolveAll(fetched, nicknames);
    }

    List<ArticleComment> getArticleComments(int pageNumber) {
        return listAllArticleCommentsPort.listAll(pageNumber, PAGE_SIZE);
    }

    private List<String> getNicknames(List<ArticleComment> fetched) {
        List<String> memberIds = fetched.stream()
                .map(ArticleComment::getMemberId)
                .toList();

        ListMembersInfoRequest request = new ListMembersInfoRequest(memberIds);
        ListMembersInfoResponse response = listMembersInfoPort.getMembers(request);
        return response.nicknames();
    }

}
