package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record GetArticleCommand(
        @NotBlank String articleId
) {
}
