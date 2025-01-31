package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record DeleteAttachmentsByArticleCommand(
        @NotBlank String articleId,
        @NotBlank String memberId
) {}
