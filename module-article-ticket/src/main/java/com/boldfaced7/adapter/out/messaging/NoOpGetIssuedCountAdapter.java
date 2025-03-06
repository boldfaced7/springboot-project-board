package com.boldfaced7.adapter.out.messaging;

import com.boldfaced7.application.port.out.GetIssuedCountPort;
import com.boldfaced7.application.port.out.GetIssuedCountRequest;
import com.boldfaced7.application.port.out.GetIssuedCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoOpGetIssuedCountAdapter implements GetIssuedCountPort {
    @Override
    public GetIssuedCountResponse getIssuedCount(GetIssuedCountRequest request) {
        return null;
    }
}
