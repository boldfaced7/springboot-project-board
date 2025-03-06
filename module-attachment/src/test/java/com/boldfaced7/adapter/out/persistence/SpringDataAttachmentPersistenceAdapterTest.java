package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.Attachment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.AttachmentTestUtil.*;

class SpringDataAttachmentPersistenceAdapterTest {

    @DisplayName("삭제할 첨부파일 객체를 전달하면, 삭제 일자가 추가된 첨부파일 객체를 반환")
    @Test
    void givenAttachment_whenDeleting_thenReturnsModifiedAttachment() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().bySave(dummy).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var request = attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);

        // when
        var result = sut.delete(request);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
    }

    @DisplayName("삭제할 첨부파일 리스트를 전달하면, 삭제 일자가 추가된 첨부파일 리스트를 반환")
    @Test
    void givenAttachments_whenDeleting_thenReturnsModifiedAttachments() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().bySaveAll(List.of(dummy)).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var request = List.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));

        // when
        var results = sut.deleteAttachments(request);

        // then
        results.forEach(result -> assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
    }

    @DisplayName("조회할 첨부파일 id를 전달하면, 첨부파일 정보가 담긴 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenReturnsArticle() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().byId(dummy).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var id = new Attachment.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isPresent()).isTrue();
        assertThat(result.get(), ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
    }

    @DisplayName("유효하지 않은 첨부파일 id를 전달하면, 빈 옵셔널 객체를 반환")
    @Test
    void givenArticleId_whenRetrieving_thenThrowsException() {
        // given
        var mocked = MockedJpaRepository.builder().byId(null).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var id = new Attachment.Id(ID);

        // when
        var result = sut.findById(id);

        // then
        Assertions.assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("조회할 페이지 넘버, 페이지 사이즈를 전달하면, 첨부파일 리스트를 반환")
    @Test
    void givenPageNumberAndPageSize_whenRetrieving_thenReturnsArticles() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().byAll(List.of(dummy)).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);

        // when
        var results = sut.listAllAttachments(PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("게시글 id와 페이지 넘버, 페이지 사이즈를 전달하면, 첨부파일 리스트를 반환")
    @Test
    void givenArticleIdAndPageNumberAndPageSize_whenRetrieving_thenReturnsArticles() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().byArticleId(List.of(dummy)).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var articleId = new Attachment.ArticleId(ARTICLE_ID);

        // when
        var results = sut.listByArticleId(articleId, PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("첨부파일 id 리스트를 전달하면, 첨부파일 리스트를 반환")
    @Test
    void givenAttachmentIds_whenRetrieving_thenReturnsArticles() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().byIdIn(List.of(dummy)).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var attachmentIds = List.of(new Attachment.Id(ID));

        // when
        var results = sut.listAttachmentsByIds(attachmentIds);

        // then
        results.forEach(result -> assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
    }

    @DisplayName("조회할 회원 id와 페이지 넘버, 페이지 사이즈를 전달하면, 첨부파일 리스트를 반환")
    @Test
    void givenMemberIdAndPageNumberAndPageSize_whenRetrieving_thenReturnsArticles() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().byMemberId(List.of(dummy)).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var memberId = new Attachment.MemberId(MEMBER_ID);

        // when
        var results = sut.listAttachmentsByMemberId(memberId, PAGE_NUMBER, PAGE_SIZE);

        // then
        results.forEach(result -> assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        Assertions.assertThat(results.size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @DisplayName("저장할 첨부파일 리스트를 전달하면, 저장된 첨부파일 정보를 반환")
    @Test
    void givenAttachments_whenSaving_thenReturnsSavedAttachments() {
        // given
        var dummy = jpaEntity(ID, "", MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().bySaveAll(List.of(dummy)).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var request = List.of(attachment("", "", MEMBER_ID, UPLOADED_NAME, STORED_NAME));

        // when
        var results = sut.saveAttachments(request);

        // then
        results.forEach(result -> assertThat(
                result, ID, "", MEMBER_ID, UPLOADED_NAME, STORED_NAME));
    }

    @DisplayName("변경할 첨부파일 리스트를 전달하면, 변경된 첨부파일 정보를 반환")
    @Test
    void givenAttachments_whenUpdating_thenReturnsModifiedAttachments() {
        // given
        var dummy = jpaEntity(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        var mocked = MockedJpaRepository.builder().bySaveAll(List.of(dummy)).build();
        var sut = new SpringDataAttachmentPersistenceAdapter(mocked);
        var request = List.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));

        // when
        var results = sut.updateAttachments(request);

        // then
        results.forEach(result -> assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
    }
}