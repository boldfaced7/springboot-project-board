package com.boldfaced7.application.port.out;

import java.util.List;

public record ListMembersInfoResponse(
        List<String> nicknames
){
    public String getNickname(int index) {
        return nicknames.get(index);
    }
}