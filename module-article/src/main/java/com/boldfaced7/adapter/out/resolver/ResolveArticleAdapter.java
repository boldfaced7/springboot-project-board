package com.boldfaced7.adapter.out.resolver;

import com.boldfaced7.adapter.out.internal.AttachmentsInfoQueryClient;
import com.boldfaced7.adapter.out.internal.AttachmentsInfoQueryRequest;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResolveArticleAdapter implements ResolveArticlePort {

    private final GetMemberInfoPort getMemberInfoPort;
    private final AttachmentsInfoQueryClient attachmentsInfoQueryClient;

    @Override
    public ResolvedArticle resolveArticle(Article article) {
        GetMemberInfoResponse memberInfo = getMemberResponse(article.getMemberId());
        List<String> attachmentUrls = getAttachmentUrls(article.getId());

        return ResolvedArticle.resolve(
                article,
                memberInfo.email(),
                memberInfo.nickname(),
                attachmentUrls
        );
    }

    private GetMemberInfoResponse getMemberResponse(String memberId) {
        GetMemberInfoRequest request = new GetMemberInfoRequest(memberId);
        return getMemberInfoPort.getMember(request)
                .orElseThrow(MemberNotFoundException::new);
    }

    private List<String> getAttachmentUrls(String articleId) {
        AttachmentsInfoQueryRequest request = new AttachmentsInfoQueryRequest(articleId);
        return attachmentsInfoQueryClient.listAttachments(request).attachmentUrls();
    }
}
