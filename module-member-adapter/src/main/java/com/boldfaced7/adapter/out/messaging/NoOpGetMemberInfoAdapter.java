package com.boldfaced7.adapter.out.messaging;

import com.boldfaced7.MessagingAdapter;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;

import java.util.Optional;

@MessagingAdapter
public class NoOpGetMemberInfoAdapter implements GetMemberInfoPort {
    @Override
    public Optional<GetMemberInfoResponse> getMember(GetMemberInfoRequest request) {
        GetMemberInfoResponse response = new GetMemberInfoResponse(
                "1",
                "boldfaced7@naver.com",
                "boldfaced7"
        );
        return Optional.of(response);
    }
}
