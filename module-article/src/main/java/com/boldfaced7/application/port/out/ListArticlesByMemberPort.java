package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Article;

import java.util.List;

public interface ListArticlesByMemberPort {
    List<Article> listByMember(Article.MemberId memberId, int pageSize, int pageNumber);
}
