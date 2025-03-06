package com.boldfaced7.application.port.out;

import java.util.Optional;

public interface GetTicketEventInfoPort {
    Optional<GetTicketEventInfoResponse> findTicketEvent(GetTicketEventInfoRequest request);
}
