package com.boldfaced7.board.member.presentation.response;

import com.boldfaced7.board.common.CustomPage;
import com.boldfaced7.board.member.application.MemberDto;
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
