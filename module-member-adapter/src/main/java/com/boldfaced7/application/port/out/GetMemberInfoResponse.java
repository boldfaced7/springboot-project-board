package com.boldfaced7.application.port.out;

public record GetMemberInfoResponse(
        String memberId,
        String email,
        String nickname
) {}
