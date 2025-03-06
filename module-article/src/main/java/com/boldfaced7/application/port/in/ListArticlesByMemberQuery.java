package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticle;

import java.util.List;

public interface ListArticlesByMemberQuery {
    List<ResolvedArticle> listMemberArticles(ListArticlesByMemberCommand command);
}
