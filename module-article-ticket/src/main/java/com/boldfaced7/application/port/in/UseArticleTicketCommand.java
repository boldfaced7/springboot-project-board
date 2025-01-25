package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record UseArticleTicketCommand(
        @NotBlank String articleTicketId,
        @NotBlank String memberId
) {}
