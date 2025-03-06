package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.ArticleTicketEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleTicketEventTestUtil.*;

class SpringDataArticleTicketEventPersistenceAdapterTest {

    @DisplayName("삭제할 이벤트 정보를 전달하면, 삭제된 이벤트 정보를 반환")
    @Test
    void givenArticle_whenDeleting_thenReturnsDeletedArticleTicketEvent() {
        // given
        var dummy = jpaEntity(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        var mocked = MockedJpaRepository.builder().save(dummy).build();
        var sut = new SpringDataArticleTicketEventEventPersistenceAdapter(mocked);
        var request = articleTicketEvent(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);

        // when
        var result = sut.delete(request);

        // then
        assertThat(result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
    }

    @DisplayName("조회할 이벤트 id를 전달하면, 이벤트 정보가 담긴 옵셔널 객체를 반환")
    @Test
    void givenEventId_whenRetrieving_thenReturnsArticleTicketEvent() {
        // given
        var dummy = jpaEntity(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        var mocked = MockedJpaRepository.builder().findById(dummy).build();
        var sut = new SpringDataArticleTicketEventEventPersistenceAdapter(mocked);
        var id = new ArticleTicketEvent.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isPresent()).isTrue();
        assertThat(result.get(), ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
    }

    @DisplayName("유효하지 않은 이벤트 id를 전달하면, 빈 옵셔널 객체를 반환")
    @Test
    void givenEventId_whenRetrieving_thenThrowsException() {
        // given
        var mocked = MockedJpaRepository.builder().build();
        var sut = new SpringDataArticleTicketEventEventPersistenceAdapter(mocked);
        var id = new ArticleTicketEvent.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("조회할 페이지 넘버, 페이지 사이즈를 전달하면, 이벤트 리스트를 반환")
    @Test
    void givenPageNumberAndPageSize_whenRetrieving_thenReturnsArticleTicketEvents() {
        // given
        var dummy = jpaEntity(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        var mocked = MockedJpaRepository.builder().findAll(List.of(dummy)).build();
        var sut = new SpringDataArticleTicketEventEventPersistenceAdapter(mocked);

        // when
        var results = sut.listAll(PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(
                result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("저장할 이벤트 정보를 전달하면, 저장된 이벤트 정보를 반환")
    @Test
    void givenArticle_whenSaving_thenReturnsSavedArticleTicketEvent() {
        // given
        var dummy = jpaEntity(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        var mocked = MockedJpaRepository.builder().save(dummy).build();
        var sut = new SpringDataArticleTicketEventEventPersistenceAdapter(mocked);
        var request = articleTicketEvent(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);

        // when
        var result = sut.save(request);

        // then
        assertThat(result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
    }

    @DisplayName("수정할 이벤트 정보를 전달하면, 수정된 이벤트 정보를 반환")
    @Test
    void givenArticle_whenUpdating_thenReturnsUpdatedArticleTicketEvent() {
        // given
        var dummy = jpaEntity(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        var mocked = MockedJpaRepository.builder().save(dummy).build();
        var sut = new SpringDataArticleTicketEventEventPersistenceAdapter(mocked);
        var request = articleTicketEvent(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);

        // when
        var result = sut.update(request);

        // then
        assertThat(result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
    }

}