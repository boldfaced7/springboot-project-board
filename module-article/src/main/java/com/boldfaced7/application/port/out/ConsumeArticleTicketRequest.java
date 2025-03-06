package com.boldfaced7.application.port.out;

import java.time.LocalDate;

public record ConsumeArticleTicketRequest(
        String memberId,
        LocalDate date
) {}
