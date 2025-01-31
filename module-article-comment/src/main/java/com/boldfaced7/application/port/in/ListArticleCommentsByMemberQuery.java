package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleComment;

import java.util.List;

public interface ListArticleCommentsByMemberQuery {
    List<ResolvedArticleComment> listArticleComments(ListArticleCommentsByMemberCommand command);
}
