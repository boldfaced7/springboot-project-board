package com.boldfaced7.adapter.out.messaging;

import com.boldfaced7.MessagingAdapter;
import com.boldfaced7.application.port.out.ListMembersInfoPort;
import com.boldfaced7.application.port.out.ListMembersInfoRequest;
import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import lombok.RequiredArgsConstructor;

@MessagingAdapter
@RequiredArgsConstructor
public class NoOpListMembersInfoAdapter implements ListMembersInfoPort {

    @Override
    public ListMembersInfoResponse getMembers(ListMembersInfoRequest request) {
        return new ListMembersInfoResponse(request.memberIds());
    }
}
