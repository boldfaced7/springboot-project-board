package com.boldfaced7.application.service;

import com.boldfaced7.AttachmentTestUtil;
import com.boldfaced7.application.port.in.SetAttachmentsArticleIdCommand;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.AttachmentTestUtil.*;

class SetAttachmentsArticleIdServiceTest {

    @DisplayName("[storeAttachments] 첨부파일 id와 게시글 id를 보내면, 수정된 첨부파일 리스트를 반환")
    @Test
    void givenSetAttachmentsArticleIdCommand_whenSetting_thenReturnsAttachments() {
        // given
        var dummy = List.of(attachment(ID, "", MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        var sut = new SetAttachmentsArticleIdService(
                attachmentIds -> dummy,
                attachments -> attachments
        );
        var command = new SetAttachmentsArticleIdCommand(MEMBER_ID, ARTICLE_ID, List.of(ID));

        // when
        var results = sut.setArticleId(command);

        // then
        results.forEach(this::assertThat);
    }

    @DisplayName("[storeAttachments] 첨부파일 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenSetting_thenThrowsException() {
        // given
        var dummy = List.of(attachment(ID, "", MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        var sut = new SetAttachmentsArticleIdService(
                attachmentIds -> dummy,
                attachments -> attachments
        );
        var command = new SetAttachmentsArticleIdCommand(WRONG_MEMBER_ID, ARTICLE_ID, List.of(ID));

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.setArticleId(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }

    private void assertThat(Attachment result) {
        AttachmentTestUtil.assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID,
                UPLOADED_NAME, STORED_NAME
        );
    }
}