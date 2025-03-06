package com.boldfaced7.adapter.out.internal;

import com.boldfaced7.MessagingAdapter;
import com.boldfaced7.application.port.out.ConsumeArticleTicketPort;
import com.boldfaced7.application.port.out.ConsumeArticleTicketRequest;
import com.boldfaced7.application.port.out.ConsumeArticleTicketResponse;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@MessagingAdapter
@RequiredArgsConstructor
public class NoOpConsumeArticleTicketAdapter implements ConsumeArticleTicketPort {
    public Optional<ConsumeArticleTicketResponse> useArticleTicket(ConsumeArticleTicketRequest request) {
        return Optional.of(new ConsumeArticleTicketResponse(request.memberId(), true));
    }
}
