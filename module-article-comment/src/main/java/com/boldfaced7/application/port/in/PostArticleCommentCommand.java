package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record PostArticleCommentCommand(
        @NotBlank String articleId,
        @NotBlank String memberId,
        @NotBlank String content
) {}
