package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record RegisterArticleTicketEventCommand(
        @NotBlank String displayName,
        @NotBlank LocalDateTime expiringAt,
        @NotBlank Long issueLimit,
        @NotBlank String memberId
) {}
