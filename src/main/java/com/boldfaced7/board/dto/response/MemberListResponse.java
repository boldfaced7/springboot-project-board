package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberListResponse {
    private List<MemberResponse> members;

    public MemberListResponse(List<MemberDto> dtos) {
        members = dtos.stream().map(MemberResponse::new).toList();
    }
}
