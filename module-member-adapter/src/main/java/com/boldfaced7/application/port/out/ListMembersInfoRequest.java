package com.boldfaced7.application.port.out;

import java.util.List;

public record ListMembersInfoRequest(
        List<String> memberIds
) {}
