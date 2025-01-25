package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record UpdateArticleTicketEventCommand(
        @NotBlank String articleTicketEventId,
        @NotBlank String displayName,
        @NotBlank String memberId
) {}
