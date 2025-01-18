package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record PostArticleCommand(
        @NotBlank String memberId,
        @NotBlank String title,
        @NotBlank String content
) {}
