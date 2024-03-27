package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberListResponse {
    private Page<MemberResponse> members;

    public MemberListResponse(Page<MemberDto> dtos) {
        members = dtos.map(MemberResponse::new);
    }
}
