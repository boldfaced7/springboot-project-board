package com.boldfaced7.application.service;

import com.boldfaced7.ArticleTestUtil;
import com.boldfaced7.application.port.in.ListArticlesCommand;
import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import com.boldfaced7.domain.ResolvedArticle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleTestUtil.*;

class ListArticlesServiceTest {

    @DisplayName("[listArticles] 페이지 번호를 보내면, 게시글 리스트를 반환")
    @Test
    void givenListArticlesCommand_whenRetrieving_thenReturnsResolvedArticles() {
        // given
        var dummy = List.of(article(ID, MEMBER_ID, TITLE, CONTENT));
        var sut = new ListArticlesService(
                (pageNumber, pageSize) -> dummy,
                request -> new ListMembersInfoResponse(List.of(NICKNAME))
        );
        var command = new ListArticlesCommand(PAGE_NUMBER);

        // when
        var results = sut.listArticles(command);

        // then
        results.forEach(this::assertThat);
    }

    private void assertThat(ResolvedArticle result) {
        ArticleTestUtil.assertThat(
                result, ID, MEMBER_ID, TITLE, CONTENT, 
                "", NICKNAME, List.of()
        );
    }
}