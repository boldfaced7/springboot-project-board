package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.GetArticleCommand;
import com.boldfaced7.application.port.in.GetArticleQuery;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Article;
import com.boldfaced7.Query;
import com.boldfaced7.domain.ResolvedArticle;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class GetArticleService implements GetArticleQuery {

    private final FindArticlePort findArticlePort;
    private final FindMemberPort findMemberPort;
    private final ListAttachmentsPort listAttachmentsPort;

    @Override
    public ResolvedArticle getArticle(GetArticleCommand command) {
        Article found = getArticle(command.articleId());
        FindMemberResponse memberResponse = getMemberResponse(found.getMemberId());
        List<String> attachmentUrls = getAttachmentUrls(command.articleId());

        return ResolvedArticle.resolve(
                found,
                memberResponse.email(),
                memberResponse.nickname(),
                attachmentUrls
        );
    }

    private Article getArticle(String articleId) {
        Article.Id id = new Article.Id(articleId);
        return findArticlePort.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private FindMemberResponse getMemberResponse(String memberId) {
        FindMemberRequest request = new FindMemberRequest(memberId);
        return findMemberPort.getMember(request)
                .orElseThrow(MemberNotFoundException::new);
    }

    private List<String> getAttachmentUrls(String articleId) {
        ListAttachmentsRequest request = new ListAttachmentsRequest(articleId);
        return listAttachmentsPort.listAttachments(request).attachmentUrls();
    }
}
