package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
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
