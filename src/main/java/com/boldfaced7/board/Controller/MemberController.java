package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.MemberRequest;
import com.boldfaced7.board.dto.response.MemberListResponse;
import com.boldfaced7.board.dto.response.MemberResponse;
import com.boldfaced7.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final String urlTemplate = "/api/members";

    @GetMapping
    public ResponseEntity<MemberListResponse> getMembers() {
        List<MemberResponse> members = memberService.getMembers()
                .stream()
                .map(MemberResponse::new)
                .toList();

        MemberListResponse response = new MemberListResponse(members);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
        MemberDto member = memberService.getMember(memberId);
        MemberResponse response = new MemberResponse(member);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    private ResponseEntity<Void> postNewMember(@RequestBody MemberRequest memberRequest) {
        Long memberId = memberService.saveMember(memberRequest.toDto());

        return ResponseEntity.created(URI.create(urlTemplate + "/" + memberId)).build();
    }

    @PatchMapping("/{memberId}")
    private ResponseEntity<Void> updateMember(
            @PathVariable Long memberId,
            @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(memberId, memberRequest.toDto());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.softDeleteMember(memberId);

        return ResponseEntity.ok().build();
    }
}
