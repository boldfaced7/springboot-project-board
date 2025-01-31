package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.DeleteAttachmentCommand;
import com.boldfaced7.exception.attachment.AttachmentNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.AttachmentTestUtil.*;

class DeleteAttachmentServiceTest {

    @DisplayName("[deleteAttachment] 첨부파일 id를 보내면, 삭제된 첨부파일 정보를 반환")
    @Test
    void givenDeleteAttachmentCommand_whenDeleting_thenReturnsDeletedAttachment() {
        // given
        var sut = new DeleteAttachmentService(
                id -> Optional.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME)),
                attachment -> attachment
        );
        var command = new DeleteAttachmentCommand(ID, MEMBER_ID);

        // when
        var result = sut.deleteAttachment(command);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME);
        Assertions.assertThat(result.getDeletedAt()).isNotNull();
    }
    
    @DisplayName("[deleteAttachment] 존재하지 않는 첨부파일 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongAttachmentId_whenDeleting_thenThrowsException() {
        // given
        var sut = new DeleteAttachmentService(
                id -> Optional.empty(),
                null
        );
        var command = new DeleteAttachmentCommand(ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteAttachment(command)
        );
        // then
        thrown.isInstanceOf(AttachmentNotFoundException.class);
    }

    @DisplayName("[deleteAttachment] 첨부파일 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenDeleting_thenThrowsException() {
        // given
        var dummy = Optional.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        var sut = new DeleteAttachmentService(id -> dummy, null);
        var command = new DeleteAttachmentCommand(ID, WRONG_MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteAttachment(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}