package com.boldfaced7.adapter.out.filestore;

import com.boldfaced7.application.port.out.ListAttachmentUrlsPort;
import com.boldfaced7.application.port.out.ListAttachmentUrlsRequest;
import com.boldfaced7.application.port.out.ListAttachmentUrlsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoOpListAttachmentUrlsAdapter implements ListAttachmentUrlsPort {
    @Override
    public ListAttachmentUrlsResponse listAttachmentUrls(ListAttachmentUrlsRequest request) {
        return null;
    }
}
