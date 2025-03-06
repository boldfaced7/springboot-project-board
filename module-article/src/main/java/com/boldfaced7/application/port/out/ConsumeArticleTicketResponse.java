package com.boldfaced7.application.port.out;

public record ConsumeArticleTicketResponse(
        String memberId,
        boolean valid
) {}
