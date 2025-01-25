package com.boldfaced7.application.service;

import com.boldfaced7.ArticleCommentTestUtil;
import com.boldfaced7.application.port.in.ListMemberArticleCommentsCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.domain.ResolvedArticleComment;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class ListMemberArticleCommentsServiceTest {

    @DisplayName("[listMemberArticleComments] 회원 id와 페이지 번호를 보내면, 해당 회원의 댓글 리스트를 반환")
    @Test
    void givenListMemberArticleCommentsCommand_whenRetrieving_thenReturnsResolvedArticleComments() {
        // given
        var dummy = List.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        var sut = new ListMemberArticleCommentsService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, "", NICKNAME)),
                (memberId, pageSize, pageNumber) -> dummy
        );
        var command = new ListMemberArticleCommentsCommand(MEMBER_ID, PAGE_NUMBER);

        // when
        var results = sut.listMemberArticleComments(command);

        // then
        results.forEach(this::assertThat);
    }

    @DisplayName("[listMemberArticleComments] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenRetrieving_thenThrowException() {
        // given
        var sut = new ListMemberArticleCommentsService(
                request -> Optional.empty(),
                null
        );
        var command = new ListMemberArticleCommentsCommand(MEMBER_ID, PAGE_NUMBER);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.listMemberArticleComments(command)
        );

        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }

    private void assertThat(ResolvedArticleComment result) {
        ArticleCommentTestUtil.assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, CONTENT, NICKNAME
        );
    }
}