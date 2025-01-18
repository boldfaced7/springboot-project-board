package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record DeleteArticleCommand(
        @NotBlank String articleId,
        @NotBlank String memberId
) {}
