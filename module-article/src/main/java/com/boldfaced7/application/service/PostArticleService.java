package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.PostArticleCommand;
import com.boldfaced7.application.port.in.PostArticleUseCase;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.domain.Article;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@UseCase
@Transactional
@RequiredArgsConstructor
public class PostArticleService implements PostArticleUseCase {

    private final GetMemberInfoPort getMemberInfoPort;
    private final ConsumeArticleTicketPort consumeArticleTicketPort;
    private final SaveArticlePort saveArticlePort;

    @Override
    public Article postArticle(PostArticleCommand command) {
        ensureMemberExists(command.memberId());
        consumeArticleTicket(command.memberId());
        return saveArticle(command);
    }

    private void ensureMemberExists(String memberId) {
        getMemberInfoPort.getMember(new GetMemberInfoRequest(memberId))
                .orElseThrow(MemberNotFoundException::new);
    }

    private void consumeArticleTicket(String memberId) {
        ConsumeArticleTicketRequest request
                = new ConsumeArticleTicketRequest(memberId, LocalDate.now());
        consumeArticleTicketPort.useArticleTicket(request)
                .orElseThrow(ArticleTicketNotFoundException::new);
    }

    private Article saveArticle(PostArticleCommand command) {
        Article generated = Article.generate(
                new Article.MemberId(command.memberId()),
                new Article.Title(command.title()),
                new Article.Content(command.content())
        );
        return saveArticlePort.save(generated);
    }
}
