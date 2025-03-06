package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleTestUtil.*;

class SpringDataArticlePersistenceAdapterTest {

    @DisplayName("저장할 게시글 정보를 전달하면, 저장된 게시글 정보를 반환")
    @Test
    void givenArticle_whenSaving_thenReturnsSavedArticle() {
        // given
        var dummy = jpaEntity(ID, MEMBER_ID, TITLE, CONTENT);
        var mocked = MockedJpaRepository.builder().bySave(dummy).build();
        var sut = new SpringDataArticlePersistenceAdapter(mocked);
        var request = article(ID, MEMBER_ID, TITLE, CONTENT);

        // when
        var result = sut.save(request);

        // then
        assertThat(result, ID, MEMBER_ID, TITLE, CONTENT);
    }

    @DisplayName("수정할 게시글 정보를 전달하면, 수정된 게시글 정보를 반환")
    @Test
    void givenArticle_whenUpdating_thenReturnsUpdatedArticle() {
        // given
        var dummy = jpaEntity(ID, MEMBER_ID, TITLE, CONTENT);
        var mocked = MockedJpaRepository.builder().bySave(dummy).build();
        var sut = new SpringDataArticlePersistenceAdapter(mocked);
        var request = article(ID, MEMBER_ID, TITLE, CONTENT);

        // when
        var result = sut.update(request);

        // then
        assertThat(result, ID, MEMBER_ID, TITLE, CONTENT);
    }

    @DisplayName("조회할 게시글 id를 전달하면, 게시글 정보가 담긴 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenReturnsArticle() {
        // given
        var dummy = jpaEntity(ID, MEMBER_ID, TITLE, CONTENT);
        var mocked = MockedJpaRepository.builder().byId(dummy).build();
        var sut = new SpringDataArticlePersistenceAdapter(mocked);
        var id = new Article.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isPresent()).isTrue();
        assertThat(result.get(), ID, MEMBER_ID, TITLE, CONTENT);
    }

    @DisplayName("유효하지 않은 게시글 id를 전달하면, 빈 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenThrowsException() {
        // given
        var mocked = MockedJpaRepository.builder().build();
        var sut = new SpringDataArticlePersistenceAdapter(mocked);
        var id = new Article.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("조회할 페이지 넘버, 페이지 사이즈를 전달하면, 게시글 리스트를 반환")
    @Test
    void givenPageNumberAndPageSize_whenRetrieving_thenReturnsArticles() {
        // given
        var dummy = jpaEntity(ID, MEMBER_ID, TITLE, CONTENT);
        var mocked = MockedJpaRepository.builder().byFindAll(List.of(dummy)).build();
        var sut = new SpringDataArticlePersistenceAdapter(mocked);

        // when
        var results = sut.listArticles(PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, MEMBER_ID, TITLE, CONTENT));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("조회할 회원 id와 페이지 넘버, 페이지 사이즈를 전달하면, 게시글 리스트를 반환")
    @Test
    void givenMemberIdAndPageNumberAndPageSize_whenRetrieving_thenReturnsArticles() {
        // given
        var dummy = jpaEntity(ID, MEMBER_ID, TITLE, CONTENT);
        var mocked = MockedJpaRepository.builder().byMemberId(List.of(dummy)).build();
        var sut = new SpringDataArticlePersistenceAdapter(mocked);
        var memberId = new Article.MemberId(MEMBER_ID);

        // when
        var results = sut.listByMember(memberId, PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, MEMBER_ID, TITLE, CONTENT));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }
}