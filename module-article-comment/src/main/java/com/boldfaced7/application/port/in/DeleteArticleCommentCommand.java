package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record DeleteArticleCommentCommand(
        @NotBlank String targetArticleCommentId,
        @NotBlank String requiringMemberId
) {}
