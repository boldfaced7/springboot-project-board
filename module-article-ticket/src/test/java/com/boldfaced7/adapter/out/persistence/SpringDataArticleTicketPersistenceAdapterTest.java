package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.ArticleTicket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class SpringDataArticleTicketPersistenceAdapterTest {

    @DisplayName("저장할 티켓 정보를 전달하면, 저장된 티켓 정보를 반환")
    @Test
    void givenArticle_whenDeleting_thenReturnsDeletedArticleTicket() {
        // given
        var dummy = jpaEntity(ID, EVENT_ID, MEMBER_ID, UNUSED);
        var mocked = MockedJpaRepository.builder().save(dummy).build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);
        var request = articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED);

        // when
        var result = sut.delete(request);

        // then
        assertThat(result, ID, EVENT_ID, MEMBER_ID);
    }

    @DisplayName("조회할 티켓 id를 전달하면, 티켓 정보가 담긴 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenReturnsArticleTicket() {
        // given
        var dummy = jpaEntity(ID, EVENT_ID, MEMBER_ID, UNUSED);
        var mocked = MockedJpaRepository.builder().findById(dummy).build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);
        var id = new ArticleTicket.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isPresent()).isTrue();
        assertThat(result.get(), ID, EVENT_ID, MEMBER_ID);
    }

    @DisplayName("유효하지 않은 티켓 id를 전달하면, 빈 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenThrowsException() {
        // given
        var mocked = MockedJpaRepository.builder().build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);
        var id = new ArticleTicket.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("조회할 페이지 넘버, 페이지 사이즈를 전달하면, 티켓 리스트를 반환")
    @Test
    void givenPageNumberAndPageSize_whenRetrieving_thenReturnsArticleTickets() {
        // given
        var dummy = jpaEntity(ID, EVENT_ID, MEMBER_ID, UNUSED);
        var mocked = MockedJpaRepository.builder().findAll(List.of(dummy)).build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);

        // when
        var results = sut.listAll(PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, EVENT_ID, MEMBER_ID));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("조회할 이벤트 id와 페이지 넘버, 페이지 사이즈를 전달하면, 티켓 리스트를 반환")
    @Test
    void givenEventIdAndPageNumberAndPageSize_whenRetrieving_thenReturnsArticleTickets() {
        // given
        var dummy = jpaEntity(ID, EVENT_ID, MEMBER_ID, UNUSED);
        var mocked = MockedJpaRepository.builder().findByEventId(List.of(dummy)).build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);
        var ticketEventId = new ArticleTicket.TicketEventId(EVENT_ID);

        // when
        var results = sut.listByEvent(ticketEventId, PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, EVENT_ID, MEMBER_ID));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("조회할 회원 id와 페이지 넘버, 페이지 사이즈를 전달하면, 티켓 리스트를 반환")
    @Test
    void givenMemberIdAndPageNumberAndPageSize_whenRetrieving_thenReturnsArticleTickets() {
        // given
        var dummy = jpaEntity(ID, EVENT_ID, MEMBER_ID, UNUSED);
        var mocked = MockedJpaRepository.builder().findByMemberId(List.of(dummy)).build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);
        var memberId = new ArticleTicket.MemberId(MEMBER_ID);

        // when
        var results = sut.listByMember(memberId, PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, EVENT_ID, MEMBER_ID));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("저장할 티켓 정보를 전달하면, 저장된 티켓 정보를 반환")
    @Test
    void givenArticle_whenSaving_thenReturnsSavedArticleTicket() {
        // given
        var dummy = jpaEntity(ID, EVENT_ID, MEMBER_ID, UNUSED);
        var mocked = MockedJpaRepository.builder().save(dummy).build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);
        var request = articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED);

        // when
        var result = sut.save(request);

        // then
        assertThat(result, ID, EVENT_ID, MEMBER_ID);
    }

    @DisplayName("수정할 티켓 정보를 전달하면, 수정된 티켓 정보를 반환")
    @Test
    void givenArticle_whenUpdating_thenReturnsUpdatedArticleTicket() {
        // given
        var dummy = jpaEntity(ID, EVENT_ID, MEMBER_ID, UNUSED);
        var mocked = MockedJpaRepository.builder().save(dummy).build();
        var sut = new SpringDataArticleTicketPersistenceAdapter(mocked);
        var request = articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED);

        // when
        var result = sut.update(request);

        // then
        assertThat(result, ID, EVENT_ID, MEMBER_ID);
    }

}