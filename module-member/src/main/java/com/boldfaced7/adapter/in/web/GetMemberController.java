package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.GetMemberCommand;
import com.boldfaced7.application.port.in.GetMemberQuery;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class GetMemberController {

    private final GetMemberQuery getMemberQuery;

    @GetMapping("/members/{memberId}")
    public ResponseEntity<GetMemberResponse> getMember(
            @PathVariable String memberId
    ) {
        Member member = getById(memberId);
        GetMemberResponse response = toResponse(member);
        return ResponseEntity.ok(response);
    }

    private Member getById(String memberId) {
        GetMemberCommand command = new GetMemberCommand(memberId);
        return getMemberQuery.getMember(command);
    }

    private GetMemberResponse toResponse(Member member) {
        return new GetMemberResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getCreatedAt()
        );
    }

    public record GetMemberResponse(
            String id,
            String email,
            String nickname,
            LocalDateTime createdAt
    ) {}
}
