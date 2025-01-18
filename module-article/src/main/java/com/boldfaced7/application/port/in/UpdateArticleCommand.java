package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record UpdateArticleCommand(
        @NotBlank String articleId,
        @NotBlank String memberId,
        @NotBlank String title,
        @NotBlank String content
) {}
