package com.boldfaced7.application.port.out;

public record ReduceAvailableTicketsResponse(
        String ticketEventId,
        boolean reduced
) {}
