package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record DeleteAttachmentCommand(
        @NotBlank String attachmentId,
        @NotBlank String memberId
) {}
