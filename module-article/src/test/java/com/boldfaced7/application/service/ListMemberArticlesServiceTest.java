package com.boldfaced7.application.service;

import com.boldfaced7.ArticleTestUtil;
import com.boldfaced7.application.port.in.ListArticlesByMemberCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.domain.ResolvedArticle;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.ArticleTestUtil.*;

class ListMemberArticlesServiceTest {

    @DisplayName("[listMemberArticles] 회원 id와 페이지 번호를 보내면, 해당 회원의 게시글 리스트를 반환")
    @Test
    void givenListMemberArticlesCommand_whenRetrieving_thenReturnsResolvedArticles() {
        // given
        var dummy = List.of(article(ID, MEMBER_ID, TITLE, CONTENT));
        var sut = new ListArticlesByMemberService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, EMAIL, NICKNAME)),
                (memberId, pageSize, pageNumber) -> dummy
        );
        var command = new ListArticlesByMemberCommand(MEMBER_ID, PAGE_NUMBER);

        // when
        var results = sut.listMemberArticles(command);

        // then
        results.forEach(this::assertThat);
    }

    @DisplayName("[listMemberArticles] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenRetrieving_thenThrowException() {
        // given
        var sut = new ListArticlesByMemberService(request -> Optional.empty(), null);
        var command = new ListArticlesByMemberCommand(MEMBER_ID, PAGE_NUMBER);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.listMemberArticles(command)
        );

        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }

    private void assertThat(ResolvedArticle result) {
        ArticleTestUtil.assertThat(
                result, ID, MEMBER_ID, TITLE, CONTENT, 
                EMPTY_EMAIL, NICKNAME, List.of()
        );
    }

}