package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicket;

import java.util.List;

public interface ListArticleTicketsByMemberPort {
    List<ArticleTicket> listByMember(ArticleTicket.MemberId memberId, int pageSize, int pageNumber);
}
