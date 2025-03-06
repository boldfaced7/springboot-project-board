package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicket;

import java.util.List;

public interface ListAllArticleTicketsPort {
    List<ArticleTicket> listAll(int pageNumber, int pageSize);
}
