package com.boldfaced7.application.port.out;

import java.time.LocalDateTime;
import java.util.List;

public record ListTicketEventsInfoResponse(
        List<String> ticketEventIds,
        List<String> displayNames,
        List<LocalDateTime> expiringAts,
        List<Long> issueLimits
) {}
