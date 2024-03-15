package com.boldfaced7.board.controller;

import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveMemberRequest;
import com.boldfaced7.board.dto.request.UpdateMemberNicknameRequest;
import com.boldfaced7.board.dto.request.UpdateMemberPasswordRequest;
import com.boldfaced7.board.dto.response.MemberListResponse;
import com.boldfaced7.board.dto.response.MemberResponse;
import com.boldfaced7.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<MemberListResponse> getMembers() {
        List<MemberDto> members = memberService.getMembers();
        MemberListResponse response = new MemberListResponse(members);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberResponse> getMember(
            @PathVariable Long memberId) {

        MemberDto member = memberService.getMember(memberId);
        MemberResponse response = new MemberResponse(member);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/signUp")
    private ResponseEntity<Void> postNewMember(
            @RequestBody @Validated SaveMemberRequest saveMemberRequest) {

        Long memberId = memberService.saveMember(saveMemberRequest.toDto());

        return ResponseEntity.created(URI.create(createUrl(memberId))).build();
    }

    @PatchMapping("/members/{memberId}/nicknames")
    private ResponseEntity<Void> updateMemberNickname(
            @PathVariable Long memberId,
            @RequestBody @Validated UpdateMemberNicknameRequest memberRequest) {

        MemberDto dto = memberRequest.toDto(memberId);
        memberService.updateMember(dto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/members/{memberId}/passwords")
    private ResponseEntity<Void> updateMemberPassword(
            @PathVariable Long memberId,
            @RequestBody @Validated UpdateMemberPasswordRequest memberRequest) {

        MemberDto dto = memberRequest.toDto(memberId);
        memberService.updateMember(dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId) {

        MemberDto dto = MemberDto.builder()
                .memberId(memberId)
                .build();

        memberService.softDeleteMember(dto);

        return ResponseEntity.ok().build();
    }

    private String createUrl(Long memberId) {
        return "/api/members/" + memberId;
    }
}
