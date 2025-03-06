package com.boldfaced7.application.port.out;

import java.util.Optional;

public interface GetMemberInfoPort {
    Optional<GetMemberInfoResponse> getMember(GetMemberInfoRequest request);
}
