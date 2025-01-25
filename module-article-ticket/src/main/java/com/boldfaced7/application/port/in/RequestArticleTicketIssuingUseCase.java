package com.boldfaced7.application.port.in;

public interface RequestArticleTicketIssuingUseCase {
    boolean requestIssuing(RequestArticleTicketIssuingCommand command);
}
