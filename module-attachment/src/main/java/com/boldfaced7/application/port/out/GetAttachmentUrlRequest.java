package com.boldfaced7.application.port.out;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record GetAttachmentUrlRequest(
        String storedName
) {
}
