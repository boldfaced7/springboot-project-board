package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAllArticleTicketEventsCommand;
import com.boldfaced7.application.port.in.ListAllArticleTicketEventsQuery;
import com.boldfaced7.application.port.out.ListAllArticleTicketEventsPort;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListAllArticleTicketEventsService implements ListAllArticleTicketEventsQuery {

    private static final int PAGE_SIZE = 20;

    private final ListAllArticleTicketEventsPort listAllArticleTicketEventsPort;

    @Override
    public List<ArticleTicketEvent> listAllEvents(ListAllArticleTicketEventsCommand command) {
        return listAllArticleTicketEventsPort
                .listAll(command.pageNumber(), PAGE_SIZE);
    }
}
