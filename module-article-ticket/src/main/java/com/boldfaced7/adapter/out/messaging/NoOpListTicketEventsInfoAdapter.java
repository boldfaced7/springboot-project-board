package com.boldfaced7.adapter.out.messaging;

import com.boldfaced7.application.port.out.ListTicketEventsInfoPort;
import com.boldfaced7.application.port.out.ListTicketEventsInfoRequest;
import com.boldfaced7.application.port.out.ListTicketEventsInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoOpListTicketEventsInfoAdapter implements ListTicketEventsInfoPort {
    @Override
    public ListTicketEventsInfoResponse listTicketEvents(ListTicketEventsInfoRequest request) {
        return null;
    }
}
