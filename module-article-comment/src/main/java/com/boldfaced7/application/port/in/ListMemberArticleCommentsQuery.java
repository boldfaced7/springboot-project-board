package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleComment;

import java.util.List;

public interface ListMemberArticleCommentsQuery {
    List<ResolvedArticleComment> listMemberArticleComments(ListMemberArticleCommentsCommand command);
}
