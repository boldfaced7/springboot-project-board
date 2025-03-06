package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.ArticleComment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class SpringDataArticleCommentPersistenceAdapterTest {

    @DisplayName("저장할 댓글 정보를 전달하면, 저장된 댓글 정보를 반환")
    @Test
    void givenArticle_whenSaving_thenReturnsSavedArticleComment() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        var mocked = MockedJpaRepository.builder().saved(dummy).build();
        var sut = new SpringDataArticleCommentPersistenceAdapter(mocked);
        var request = articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT);

        // when
        var result = sut.save(request);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT);
    }

    @DisplayName("수정할 댓글 정보를 전달하면, 수정된 댓글 정보를 반환")
    @Test
    void givenArticle_whenUpdating_thenReturnsUpdatedArticleComment() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        var mocked = MockedJpaRepository.builder().saved(dummy).build();
        var sut = new SpringDataArticleCommentPersistenceAdapter(mocked);
        var request = articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT);

        // when
        var result = sut.update(request);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT);
    }

    @DisplayName("조회할 댓글 id를 전달하면, 댓글 정보가 담긴 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenReturnsArticleComment() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        var mocked = MockedJpaRepository.builder().byId(dummy).build();
        var sut = new SpringDataArticleCommentPersistenceAdapter(mocked);
        var id = new ArticleComment.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isPresent()).isTrue();
        assertThat(result.get(), ID, ARTICLE_ID, MEMBER_ID, CONTENT);
    }

    @DisplayName("유효하지 않은 댓글 id를 전달하면, 빈 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenThrowsException() {
        // given
        var mocked = MockedJpaRepository.builder().build();
        var sut = new SpringDataArticleCommentPersistenceAdapter(mocked);
        var id = new ArticleComment.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("조회할 페이지 넘버, 페이지 사이즈를 전달하면, 댓글 리스트를 반환")
    @Test
    void givenPageNumberAndPageSize_whenRetrieving_thenReturnsArticleComments() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        var mocked = MockedJpaRepository.builder().all(List.of(dummy)).build();
        var sut = new SpringDataArticleCommentPersistenceAdapter(mocked);

        // when
        var results = sut.listAll(PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("조회할 게시글 id와 페이지 넘버, 페이지 사이즈를 전달하면, 댓글 리스트를 반환")
    @Test
    void givenArticleIdAndPageNumberAndPageSize_whenRetrieving_thenReturnsArticleComments() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        var mocked = MockedJpaRepository.builder().byArticleId(List.of(dummy)).build();
        var sut = new SpringDataArticleCommentPersistenceAdapter(mocked);
        var articleId = new ArticleComment.ArticleId(ARTICLE_ID);

        // when
        var results = sut.listArticleComments(articleId, PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("조회할 회원 id와 페이지 넘버, 페이지 사이즈를 전달하면, 댓글 리스트를 반환")
    @Test
    void givenMemberIdAndPageNumberAndPageSize_whenRetrieving_thenReturnsArticleComments() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        var mocked = MockedJpaRepository.builder().byMemberId(List.of(dummy)).build();
        var sut = new SpringDataArticleCommentPersistenceAdapter(mocked);
        var memberId = new ArticleComment.MemberId(MEMBER_ID);

        // when
        var results = sut.listMemberArticleComments(memberId, PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }
}