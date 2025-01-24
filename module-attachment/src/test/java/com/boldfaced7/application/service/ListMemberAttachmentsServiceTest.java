package com.boldfaced7.application.service;

import com.boldfaced7.AttachmentTestUtil;
import com.boldfaced7.application.port.in.ListMemberAttachmentsCommand;
import com.boldfaced7.domain.ResolvedAttachment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.AttachmentTestUtil.*;

class ListMemberAttachmentsServiceTest {

    @DisplayName("[lisMemberAttachments] 회원 Id와 페이지 번호를 보내면, 게시글 리스트를 반환")
    @Test
    void givenListAttachmentsCommand_whenRetrieving_thenReturnsResolvedAttachments() {
        // given
        var dummy = List.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));
        var sut = new ListMemberAttachmentsService(
                (memberId, pageNumber, pageSize) -> dummy,
                attachments -> ResolvedAttachment.resolveAll(dummy, List.of(URL))
        );
        var command = new ListMemberAttachmentsCommand(MEMBER_ID, PAGE_NUMBER);

        // when
        var results = sut.listMemberAttachments(command);

        // then
        results.forEach(this::assertThat);
    }

    private void assertThat(ResolvedAttachment result) {
        AttachmentTestUtil.assertThat(
                result, ID, ARTICLE_ID, MEMBER_ID,
                UPLOADED_NAME, STORED_NAME, URL
        );
    }
}