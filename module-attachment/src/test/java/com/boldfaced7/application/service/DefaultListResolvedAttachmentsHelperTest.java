package com.boldfaced7.application.service;

import com.boldfaced7.AttachmentTestUtil;
import com.boldfaced7.application.port.out.ListAttachmentUrlsResponse;
import com.boldfaced7.domain.ResolvedAttachment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.AttachmentTestUtil.*;
import static com.boldfaced7.AttachmentTestUtil.URL;

class DefaultListResolvedAttachmentsHelperTest {
    @DisplayName("[listResolvedAttachments] 첨부파일 리스트를 보내면, 리졸브된 첨부파일 리스트를 반환")
    @Test
    void givenAttachments_whenRetrieving_thenReturnsResolvedAttachments() {
        // given
        var sut = new DefaultListResolvedAttachmentsHelper(
                request -> new ListAttachmentUrlsResponse(List.of(URL))
        );
        var attachments = List.of(attachment(ID, ARTICLE_ID, MEMBER_ID, UPLOADED_NAME, STORED_NAME));

        // when
        var results = sut.listResolvedAttachments(attachments);

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