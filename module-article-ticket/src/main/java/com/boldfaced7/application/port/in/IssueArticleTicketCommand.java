package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record IssueArticleTicketCommand(
        @NotBlank String ticketEventId,
        @NotBlank String memberId
) {}
