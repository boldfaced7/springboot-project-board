package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.GetAttachmentCommand;
import com.boldfaced7.application.port.out.GetAttachmentUrlResponse;
import com.boldfaced7.exception.attachment.AttachmentNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.AttachmentTestUtil.*;

class GetAttachmentServiceTest {

    @DisplayName("[getAttachment] 첨부파일 id를 보내면, 첨부파일 정보를 반환")
    @Test
    void givenGetAttachmentCommand_whenRetrieving_thenReturnsResolvedAttachment() {
        // given
        var sut = new GetAttachmentService(
                id -> Optional.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME)),
                request -> new GetAttachmentUrlResponse(URL, VALID)
        );
        var command = new GetAttachmentCommand(ID);

        // when
        var result = sut.getAttachment(command);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME, URL);
    }

    @DisplayName("[getAttachment] 존재하지 않는 첨부파일 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongAttachmentId_whenRetrieving_thenThrowsException() {
        // given
        var sut = new GetAttachmentService(
                id -> Optional.empty(),
                null
        );
        var command = new GetAttachmentCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getAttachment(command)
        );
        // then
        thrown.isInstanceOf(AttachmentNotFoundException.class);
    }

    @DisplayName("[getAttachment] 첨부파일 url이 존재하지 않으면, 예외를 던짐")
    @Test
    void givenUrlNotExists_whenRetrieving_thenThrowsException() {
        // given
        var sut = new GetAttachmentService(
                id -> Optional.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME)),
                request -> new GetAttachmentUrlResponse(URL, INVALID)
        );
        var command = new GetAttachmentCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getAttachment(command)
        );
        // then
        thrown.isInstanceOf(AttachmentNotFoundException.class);
    }
}