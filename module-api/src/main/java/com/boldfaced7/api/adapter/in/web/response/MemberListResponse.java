package com.boldfaced7.api.adapter.in.web.response;

import com.boldfaced7.member.application.MemberDto;
import com.boldfaced7.common.CustomPage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberListResponse {
    private CustomPage<MemberResponse> members;

    public MemberListResponse(CustomPage<MemberDto> dtos) {
        members = dtos.map(MemberResponse::new);
    }
}
