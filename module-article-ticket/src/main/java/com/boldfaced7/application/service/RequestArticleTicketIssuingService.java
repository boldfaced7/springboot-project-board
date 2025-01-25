package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.RequestArticleTicketIssuingCommand;
import com.boldfaced7.application.port.in.RequestArticleTicketIssuingUseCase;
import com.boldfaced7.application.port.out.*;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RequestArticleTicketIssuingService implements RequestArticleTicketIssuingUseCase {

    private final ReduceAvailableTicketsPort reduceAvailableTicketsPort;

    @Override
    public boolean requestIssuing(RequestArticleTicketIssuingCommand command) {
        ReduceAvailableTicketsRequest request
                = new ReduceAvailableTicketsRequest(command.ticketEventId());
        return reduceAvailableTicketsPort.reduceAvailable(request).reduced();
    }
}
