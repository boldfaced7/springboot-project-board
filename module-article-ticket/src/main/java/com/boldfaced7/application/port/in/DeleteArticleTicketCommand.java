package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record DeleteArticleTicketCommand(
        @NotBlank String targetArticleTicketId,
        @NotBlank String requiringMemberId
) {}
