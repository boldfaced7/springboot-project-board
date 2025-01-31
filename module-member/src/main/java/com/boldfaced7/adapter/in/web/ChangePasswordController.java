package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ChangePasswordCommand;
import com.boldfaced7.application.port.in.ChangePasswordUseCase;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class ChangePasswordController {

    private final ChangePasswordUseCase changePasswordUseCase;

    @PatchMapping("/members/{memberId}/password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @PathVariable String memberId,
            @RequestHeader("X-Member-Id") String requiringMemberId,
            @RequestBody ChangePasswordRequest request
    ) {
        Member updated = changePassword(memberId, requiringMemberId, request.password());
        ChangePasswordResponse response = toResponse(updated);
        return ResponseEntity.ok(response);
    }

    private Member changePassword(String memberId, String requiringMemberId, String password) {
        ChangePasswordCommand command
                = new ChangePasswordCommand(memberId, requiringMemberId, password);
        return changePasswordUseCase.changePassword(command);
    }

    private ChangePasswordResponse toResponse(Member member) {
        return new ChangePasswordResponse(member.getId(), member.getUpdatedAt());
    }

    public record ChangePasswordRequest(
            String password
    ) {}

    public record ChangePasswordResponse(
            String id,
            LocalDateTime updatedAt
    ) {}
}
