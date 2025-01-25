package com.boldfaced7.application.service;

import com.boldfaced7.ArticleCommentTestUtil;
import com.boldfaced7.application.port.in.ListArticleCommentsCommand;
import com.boldfaced7.application.port.out.GetArticleInfoResponse;
import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import com.boldfaced7.domain.ResolvedArticleComment;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class ListArticleCommentsServiceTest {

    @DisplayName("[listAllArticleComments] 페이지 번호를 보내면, 댓글 리스트를 반환")
    @Test
    void givenListArticleCommentsCommand_whenRetrieving_thenReturnsResolvedArticleComments() {
        // given
        var dummy = List.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        var sut = new ListArticleCommentsService(
                request -> Optional.of(new GetArticleInfoResponse(ARTICLE_ID, VALID)),
                (articleId, pageNumber, pageSize) -> dummy,
                request -> new ListMembersInfoResponse(List.of(NICKNAME))
        );
        var command = new ListArticleCommentsCommand(ARTICLE_ID, PAGE_NUMBER);

        // when
        var results = sut.listArticleComments(command);

        // then
        results.forEach(this::assertThat);
    }

    @DisplayName("[listAllArticleComments] 존재하지 않는 게시글 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenRetrieving_thenThrowsException() {
        // given
        var sut = new ListArticleCommentsService(
                request -> Optional.empty(),
                null,
                null
        );
        var command = new ListArticleCommentsCommand(ARTICLE_ID, PAGE_NUMBER);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.listArticleComments(command)
        );

        // then
        thrown.isInstanceOf(ArticleNotFoundException.class);
    }

    private void assertThat(ResolvedArticleComment result) {
        ArticleCommentTestUtil.assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, CONTENT, NICKNAME
        );
    }
}