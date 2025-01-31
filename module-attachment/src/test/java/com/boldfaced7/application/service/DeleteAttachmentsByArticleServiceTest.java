package com.boldfaced7.application.service;

import com.boldfaced7.AttachmentTestUtil;
import com.boldfaced7.application.port.in.DeleteAttachmentsByArticleCommand;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.AttachmentTestUtil.*;

class DeleteAttachmentsByArticleServiceTest {

    @DisplayName("[deleteAttachments] 삭제할 첨부파일의 id를 보내면, 삭제된 게시글 정보를 반환")
    @Test
    void givenDeleteAttachmentsCommand_whenDeleting_thenReturnsSavedArticle() {
        // given
        var dummy = List.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        var sut = new DeleteAttachmentsByArticleService(
                ids -> dummy,
                attachments -> attachments
        );
        var command = new DeleteAttachmentsByArticleCommand(ARTICLE_ID, MEMBER_ID);

        // when
        var result = sut.deleteAttachments(command);

        // then
        result.forEach(this::assertThat);
    }

    private void assertThat(Attachment result) {
        AttachmentTestUtil.assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        Assertions.assertThat(result.getDeletedAt()).isNotNull();
    }

    @DisplayName("[deleteAttachments] 첨부파일 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenDeleting_thenThrowsException() {
        // given
        var dummy = List.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        var sut = new DeleteAttachmentsByArticleService(
                ids -> dummy,
                attachments -> attachments
        );
        var command = new DeleteAttachmentsByArticleCommand(ARTICLE_ID, WRONG_MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteAttachments(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}