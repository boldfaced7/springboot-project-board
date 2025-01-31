package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record ChangeDisplayNameCommand(
        @NotBlank String articleTicketEventId,
        @NotBlank String displayName,
        @NotBlank String memberId
) {}
