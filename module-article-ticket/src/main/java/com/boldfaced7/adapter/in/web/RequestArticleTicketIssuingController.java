package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.RequestArticleTicketIssuingCommand;
import com.boldfaced7.application.port.in.RequestArticleTicketIssuingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@WebAdapter
@RequiredArgsConstructor
public class RequestArticleTicketIssuingController {

    private final RequestArticleTicketIssuingUseCase requestArticleTicketIssuingUseCase;

    @PostMapping("/tickets")
    public ResponseEntity<Void> requestIssuing(
            @RequestBody ArticleTicketIssuingRequest request,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        requestByEvent(request.ticketEventId(), memberId);
        return ResponseEntity.ok().build();
    }

    private void requestByEvent(String ticketEventId, String memberId) {
        RequestArticleTicketIssuingCommand command
                = new RequestArticleTicketIssuingCommand(ticketEventId, memberId);
        requestArticleTicketIssuingUseCase.requestIssuing(command);
    }

    public record ArticleTicketIssuingRequest(String ticketEventId) {}
}