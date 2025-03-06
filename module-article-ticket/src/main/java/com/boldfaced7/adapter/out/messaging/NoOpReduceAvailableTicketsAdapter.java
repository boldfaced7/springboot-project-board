package com.boldfaced7.adapter.out.messaging;

import com.boldfaced7.application.port.out.ReduceAvailableTicketsPort;
import com.boldfaced7.application.port.out.ReduceAvailableTicketsRequest;
import com.boldfaced7.application.port.out.ReduceAvailableTicketsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoOpReduceAvailableTicketsAdapter implements ReduceAvailableTicketsPort {
    @Override
    public ReduceAvailableTicketsResponse reduceAvailable(ReduceAvailableTicketsRequest request) {
        return null;
    }
}
