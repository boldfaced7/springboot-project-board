package com.boldfaced7.adapter.out.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AttachmentsInfoQueryClient {
    public AttachmentsInfoQueryResponse listAttachments(AttachmentsInfoQueryRequest request) {
        return new AttachmentsInfoQueryResponse(request.articleId(), List.of());
    }
}
