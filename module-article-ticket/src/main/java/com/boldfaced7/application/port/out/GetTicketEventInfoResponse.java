package com.boldfaced7.application.port.out;

import java.time.LocalDateTime;

public record GetTicketEventInfoResponse(
        String ticketEventId,
        String displayName,
        LocalDateTime expiringAt,
        Long issueLimit,
        boolean valid
) {}
