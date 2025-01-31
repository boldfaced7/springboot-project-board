package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.RegisterMemberCommand;
import com.boldfaced7.application.port.in.RegisterMemberUseCase;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class RegisterMemberController {

    private final RegisterMemberUseCase registerMemberUseCase;

    @PostMapping("/members")
    public ResponseEntity<RegisterMemberResponse> registerMember(
            @RequestBody RegisterMemberRequest request
    ) {
        Member registered = register(request.email(), request.password(), request.nickname());
        RegisterMemberResponse response = toResponse(registered);
        return ResponseEntity.ok(response);
    }

    private Member register(String email, String password, String nickname) {
        RegisterMemberCommand command
                = new RegisterMemberCommand(email, password, nickname);
        return registerMemberUseCase.registerMember(command);
    }

    private RegisterMemberResponse toResponse(Member member) {
        return new RegisterMemberResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getCreatedAt()
        );
    }

    public record RegisterMemberRequest(
            String email,
            String password,
            String nickname
    ) {}

    public record RegisterMemberResponse(
            String id,
            String email,
            String nickname,
            LocalDateTime createdAt
    ) {}
}