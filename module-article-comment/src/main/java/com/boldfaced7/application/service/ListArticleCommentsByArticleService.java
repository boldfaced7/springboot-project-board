package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListArticleCommentsByArticleCommand;
import com.boldfaced7.application.port.in.ListArticleCommentsByArticleQuery;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.application.port.out.ListMembersInfoPort;
import com.boldfaced7.application.port.out.ListMembersInfoRequest;
import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.domain.ResolvedArticleComment;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListArticleCommentsByArticleService implements ListArticleCommentsByArticleQuery {

    private static final int PAGE_SIZE = 20;

    private final GetArticleInfoPort getArticleInfoPort;
    private final ListArticleCommentsPort listArticleCommentsPort;
    private final ListMembersInfoPort listMembersInfoPort;

    @Override
    public List<ResolvedArticleComment> listArticleComments(ListArticleCommentsByArticleCommand command) {
        ensureArticleExists(command.articleId());
        List<ArticleComment> fetched = getArticleComments(command);
        return resolveAll(fetched);
    }

    private void ensureArticleExists(String articleId) {
        GetArticleInfoRequest request = new GetArticleInfoRequest(articleId);
        getArticleInfoPort.findArticle(request)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private List<ArticleComment> getArticleComments(ListArticleCommentsByArticleCommand command) {
        return listArticleCommentsPort.listArticleComments(
                new ArticleComment.ArticleId(command.articleId()),
                command.pageNumber(),
                PAGE_SIZE
        );
    }

    private List<ResolvedArticleComment> resolveAll(List<ArticleComment> fetched) {
        List<String> nicknames = getNicknames(fetched);
        return ResolvedArticleComment.resolveAll(fetched, nicknames);
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
