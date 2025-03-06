package com.boldfaced7.adapter.out.filestore;

import com.boldfaced7.application.port.out.GetAttachmentUrlPort;
import com.boldfaced7.application.port.out.GetAttachmentUrlRequest;
import com.boldfaced7.application.port.out.GetAttachmentUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoOpGetAttachmentUrlAdapter implements GetAttachmentUrlPort {

    @Override
    public GetAttachmentUrlResponse getAttachmentUrl(GetAttachmentUrlRequest request) {
        return null;
    }
}
