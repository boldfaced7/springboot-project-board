package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record DeleteArticleTicketEventCommand(
        @NotBlank String articleTicketEventId,
        @NotBlank String memberId
) {}
