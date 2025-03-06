package com.boldfaced7.adapter.out.internal;

import java.util.List;

public record AttachmentsInfoQueryResponse(
        String articleId,
        List<String> attachmentUrls
) {}
