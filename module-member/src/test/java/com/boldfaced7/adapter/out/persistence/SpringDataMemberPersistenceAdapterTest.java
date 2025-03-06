package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.MemberTestUtil.*;
import static com.boldfaced7.MemberTestUtil.ID;

class SpringDataMemberPersistenceAdapterTest {

    @DisplayName("저장할 댓글 정보를 전달하면, 저장된 댓글 정보를 반환")
    @Test
    void givenArticle_whenSaving_thenReturnsSavedMember() {
        // given
        var mocked = MockedJpaRepository.builder()
                .save(entity -> setField(entity, "id", 1L))
                .build();
        var sut = new SpringDataMemberPersistenceAdapter(mocked);
        var request = member(null, EMAIL, PASSWORD, NICKNAME);

        // when
        var result = sut.save(request);

        // then
        assertThat(result, ID, EMAIL, PASSWORD, NICKNAME);
    }

    @DisplayName("수정할 댓글 정보를 전달하면, 수정된 댓글 정보를 반환")
    @Test
    void givenArticle_whenUpdating_thenReturnsUpdatedMember() {
        // given
        var mocked = MockedJpaRepository.builder()
                .save(entity -> entity)
                .build();
        var sut = new SpringDataMemberPersistenceAdapter(mocked);
        var request = member(ID, EMAIL, NEW_PASSWORD, NEW_NICKNAME);

        // when
        var result = sut.update(request);

        // then
        assertThat(result, ID, EMAIL, NEW_PASSWORD, NEW_NICKNAME);
    }

    @DisplayName("조회할 댓글 id를 전달하면, 댓글 정보가 담긴 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenReturnsMember() {
        // given
        var dummy = jpaEntity(ID, EMAIL, PASSWORD, NICKNAME);
        var mocked = MockedJpaRepository.builder()
                .findById(id -> Optional.of(dummy))
                .build();
        var sut = new SpringDataMemberPersistenceAdapter(mocked);
        var id = new Member.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isPresent()).isTrue();
        assertThat(result.get(), ID, EMAIL, PASSWORD, NICKNAME);
    }

    @DisplayName("유효하지 않은 댓글 id를 전달하면, 빈 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenThrowsException() {
        // given
        var mocked = MockedJpaRepository.builder()
                .findById(id -> Optional.empty())
                .build();
        var sut = new SpringDataMemberPersistenceAdapter(mocked);
        var id = new Member.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("조회할 페이지 넘버, 페이지 사이즈를 전달하면, 댓글 리스트를 반환")
    @Test
    void givenPageNumberAndPageSize_whenRetrieving_thenReturnsMembers() {
        // given
        var dummy = jpaEntity(ID, EMAIL, PASSWORD, NICKNAME);
        var mocked = MockedJpaRepository.builder()
                .findAll(pageable -> List.of(dummy))
                .build();
        var sut = new SpringDataMemberPersistenceAdapter(mocked);

        // when
        var results = sut.listAll(PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(result, ID, EMAIL, PASSWORD, NICKNAME));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }
}