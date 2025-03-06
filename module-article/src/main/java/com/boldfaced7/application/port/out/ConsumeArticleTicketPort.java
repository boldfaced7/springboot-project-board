package com.boldfaced7.application.port.out;

import java.util.Optional;

public interface ConsumeArticleTicketPort {
    Optional<ConsumeArticleTicketResponse> useArticleTicket(ConsumeArticleTicketRequest request);
}
