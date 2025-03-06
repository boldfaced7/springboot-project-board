package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ResolvedArticle;

import java.util.List;

import static com.boldfaced7.domain.Article.*;

public interface ListArticleCachesByMemberPort {
    List<ResolvedArticle> listByMember(MemberId memberId, int pageSize, int pageNumber);
}
