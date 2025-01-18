package com.boldfaced7.application.service;

import com.boldfaced7.ArticleCommentTestUtil;
import com.boldfaced7.application.port.in.ListAllArticleCommentsCommand;
import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import com.boldfaced7.domain.ResolvedArticleComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class ListAllArticleCommentsServiceTest {

    @DisplayName("[listAllArticleComments] 페이지 번호를 보내면, 댓글 리스트를 반환")
    @Test
    void givenListArticleCommentsCommand_whenRetrieving_thenReturnsResolvedArticleComments() {
        // given
        var dummy = List.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        var sut = new ListAllArticleCommentsService(
                (pageNumber, pageSize) -> dummy,
                request -> new ListMembersInfoResponse(List.of(NICKNAME))
        );
        var command = new ListAllArticleCommentsCommand(PAGE_NUMBER);

        // when
        var results = sut.listAllArticleComments(command);

        // then
        results.forEach(this::assertThat);
    }

    private void assertThat(ResolvedArticleComment result) {
        ArticleCommentTestUtil.assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, CONTENT, NICKNAME);
    }
}