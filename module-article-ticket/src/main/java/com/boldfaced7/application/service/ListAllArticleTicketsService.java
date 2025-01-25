package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAllArticleTicketsCommand;
import com.boldfaced7.application.port.in.ListAllArticleTicketsQuery;
import com.boldfaced7.application.port.in.ListResolvedArticleTicketsHelper;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.domain.ResolvedArticleTicket;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListAllArticleTicketsService implements ListAllArticleTicketsQuery {

    private static final int PAGE_SIZE = 20;

    private final ListAllArticleTicketsPort listAllArticleTicketsPort;
    private final ListResolvedArticleTicketsHelper listResolvedArticleTicketsHelper;

    @Override
    public List<ResolvedArticleTicket> listAllArticleTickets(ListAllArticleTicketsCommand command) {
        List<ArticleTicket> articleTickets = listAllArticleTicketsPort
                .listAll(command.pageNumber(), PAGE_SIZE);

        return listResolvedArticleTicketsHelper
                .listResolvedArticleTickets(articleTickets);
    }
}
