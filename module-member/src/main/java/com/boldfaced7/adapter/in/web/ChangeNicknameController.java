package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ChangeNicknameCommand;
import com.boldfaced7.application.port.in.ChangeNicknameUseCase;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@WebAdapter
@RequiredArgsConstructor
public class ChangeNicknameController {

    private final ChangeNicknameUseCase changeNicknameUseCase;

    @PatchMapping("/members/{memberId}/nickname")
    public ResponseEntity<ChangeNicknameResponse> changeNickname(
            @PathVariable String memberId,
            @RequestHeader("X-Member-Id") String requiringMemberId,
            @RequestBody ChangeNicknameRequest request
    ) {
        Member changed = changeNickname(memberId, requiringMemberId, request.nickname());
        ChangeNicknameResponse response = toResponse(changed);
        return ResponseEntity.ok(response);
    }

    private Member changeNickname(String memberId, String requiringMemberId, String nickname) {
        ChangeNicknameCommand command = new ChangeNicknameCommand(memberId, requiringMemberId, nickname);
        return changeNicknameUseCase.changeNickname(command);
    }

    private ChangeNicknameResponse toResponse(Member member) {
        return new ChangeNicknameResponse(member.getId(), member.getNickname());
    }

    public record ChangeNicknameRequest(
            String nickname
    ) {}

    public record ChangeNicknameResponse(
            String memberId,
            String nickname
    ) {}
}