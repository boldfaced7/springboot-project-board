package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleComment;

import java.util.List;

public interface ListMemberArticleCommentsPort {
    List<ArticleComment> listMemberArticleComments(ArticleComment.MemberId memberId, int pageSize, int pageNumber);
}
