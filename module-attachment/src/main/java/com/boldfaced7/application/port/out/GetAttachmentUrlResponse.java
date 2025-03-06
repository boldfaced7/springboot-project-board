package com.boldfaced7.application.port.out;

import java.util.List;

public record GetAttachmentUrlResponse(
        String attachmentUrl,
        boolean valid
) {
}
