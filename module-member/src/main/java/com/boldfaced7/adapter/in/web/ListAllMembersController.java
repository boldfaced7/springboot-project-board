package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListAllMembersCommand;
import com.boldfaced7.application.port.in.ListAllMembersQuery;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListAllMembersController {

    private final ListAllMembersQuery listAllMembersQuery;

    @GetMapping("/members")
    public ResponseEntity<List<ListAllMembersResponse>> listAllMembers(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<Member> members = listAll(page);
        List<ListAllMembersResponse> responses = toResponses(members);
        return ResponseEntity.ok(responses);
    }

    private List<Member> listAll(int page) {
        ListAllMembersCommand command = new ListAllMembersCommand(page);
        return listAllMembersQuery.listAllMembers(command);
    }

    private List<ListAllMembersResponse> toResponses(List<Member> members) {
        return members.stream()
                .map(member -> new ListAllMembersResponse(
                        member.getId(),
                        member.getEmail(),
                        member.getNickname(),
                        member.getCreatedAt()
                ))
                .toList();
    }

    public record ListAllMembersResponse(
            String id,
            String email,
            String nickname,
            LocalDateTime createdAt
    ) {}
}