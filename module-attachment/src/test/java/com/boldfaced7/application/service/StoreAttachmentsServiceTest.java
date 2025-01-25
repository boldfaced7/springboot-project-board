package com.boldfaced7.application.service;

import com.boldfaced7.AttachmentTestUtil;
import com.boldfaced7.application.port.in.StoreAttachmentsCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.application.port.out.StoreFilesResponse;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.AttachmentTestUtil.*;

class StoreAttachmentsServiceTest {

    @DisplayName("[storeAttachments] 페이지 번호를 보내면, 저장된 첨부파일 리스트를 반환")
    @Test
    void givenListAttachmentsCommand_whenSaving_thenReturnsAttachments() {
        // given
        var dummy = List.of(attachment("", "", MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        var sut = new StoreAttachmentsService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, UPLOADED_NAME, STORED_NAME)),
                request -> new StoreFilesResponse(List.of(STORED_NAME)),
                attachments -> dummy.stream().map(a -> setField(a, "id", ID)).toList()
        );
        var command = new StoreAttachmentsCommand(MEMBER_ID, List.of());

        // when
        var results = sut.storeAttachments(command);

        // then
        results.forEach(this::assertThat);
    }

    @DisplayName("[storeAttachments] 존재하지 않는 회원 Id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenSaving_thenThrowsException() {
        // given
        var sut = new StoreAttachmentsService(
                request -> Optional.empty(),
                null,
                null
        );
        var command = new StoreAttachmentsCommand(MEMBER_ID, List.of());

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.storeAttachments(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }

    private void assertThat(Attachment result) {
        AttachmentTestUtil.assertThat(
                result, ID, "", MEMBER_ID,
                UPLOADED_NAME, STORED_NAME
        );
    }
}