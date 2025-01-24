package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ListResolvedAttachmentsHelper;
import com.boldfaced7.application.port.out.ListAttachmentUrlsPort;
import com.boldfaced7.application.port.out.ListAttachmentUrlsRequest;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefaultListResolvedAttachmentsHelper implements ListResolvedAttachmentsHelper {

    private final ListAttachmentUrlsPort listAttachmentUrlsPort;

    @Override
    public List<ResolvedAttachment> listResolvedAttachments(List<Attachment> attachments) {
        List<String> urls = getUrls(attachments);
        return ResolvedAttachment.resolveAll(attachments, urls);
    }

    private List<String> getUrls(List<Attachment> attachments) {
        ListAttachmentUrlsRequest request = attachments.stream()
                .map(Attachment::getStoredName)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        ListAttachmentUrlsRequest::new
                ));

        return listAttachmentUrlsPort
                .listAttachmentUrls(request)
                .attachmentUrls();
    }
}
