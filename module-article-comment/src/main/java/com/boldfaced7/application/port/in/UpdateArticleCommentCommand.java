package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record UpdateArticleCommentCommand(
        @NotBlank String articleCommentId,
        @NotBlank String memberId,
        @NotBlank String content
) {}
