package com.boldfaced7.api.adapter.in.web;

import com.boldfaced7.common.CustomPage;
import com.boldfaced7.member.application.MemberDto;
import com.boldfaced7.api.adapter.in.web.request.SaveMemberRequest;
import com.boldfaced7.api.adapter.in.web.request.UpdateMemberNicknameRequest;
import com.boldfaced7.api.adapter.in.web.request.UpdateMemberPasswordRequest;
import com.boldfaced7.api.adapter.in.web.response.MemberListResponse;
import com.boldfaced7.api.adapter.in.web.response.MemberResponse;
import com.boldfaced7.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<MemberListResponse> getMembers(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        CustomPage<MemberDto> members = memberService.getMembers(pageable);
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
