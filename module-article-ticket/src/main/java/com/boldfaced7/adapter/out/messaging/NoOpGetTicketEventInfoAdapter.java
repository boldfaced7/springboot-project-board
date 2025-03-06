package com.boldfaced7.adapter.out.messaging;

import com.boldfaced7.MessagingAdapter;
import com.boldfaced7.application.port.out.GetTicketEventInfoPort;
import com.boldfaced7.application.port.out.GetTicketEventInfoRequest;
import com.boldfaced7.application.port.out.GetTicketEventInfoResponse;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@MessagingAdapter
@RequiredArgsConstructor
public class NoOpGetTicketEventInfoAdapter implements GetTicketEventInfoPort {

    @Override
    public Optional<GetTicketEventInfoResponse> findTicketEvent(GetTicketEventInfoRequest request) {
        return Optional.empty();
    }
}
