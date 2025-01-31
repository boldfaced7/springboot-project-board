package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.DeleteMemberCommand;
import com.boldfaced7.application.port.in.DeleteMemberUseCase;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class DeleteMemberController {

    private final DeleteMemberUseCase deleteMemberUseCase;

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<DeleteMemberResponse> deleteMember(
            @PathVariable String memberId,
            @RequestHeader("X-Member-Id") String requiringMemberId
    ) {
        Member deleted = deleteById(memberId, requiringMemberId);
        DeleteMemberResponse response = toResponse(deleted);
        return ResponseEntity.ok(response);
    }

    private Member deleteById(String memberId, String requiringMemberId) {
        DeleteMemberCommand command = new DeleteMemberCommand(memberId, requiringMemberId);
        return deleteMemberUseCase.deleteMember(command);
    }

    private DeleteMemberResponse toResponse(Member member) {
        return new DeleteMemberResponse(member.getId(), member.getDeletedAt());
    }

    public record DeleteMemberResponse(
            String id,
            LocalDateTime deletedAt
    ) {}
}