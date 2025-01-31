package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.CheckNicknameDuplicationCommand;
import com.boldfaced7.application.port.in.CheckNicknameDuplicationQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class CheckNicknameDuplicationController {

    private final CheckNicknameDuplicationQuery checkNicknameDuplicationQuery;

    @GetMapping("/members/check-nickname")
    public ResponseEntity<CheckDuplicationResponse> checkNicknameDuplication(
            @RequestParam String nickname
    ) {
        boolean duplicated = isDuplicated(nickname);
        CheckDuplicationResponse response = toResponse(duplicated);
        return ResponseEntity.ok(response);
    }

    private boolean isDuplicated(String nickname) {
        CheckNicknameDuplicationCommand command
                = new CheckNicknameDuplicationCommand(nickname);
        return checkNicknameDuplicationQuery.isDuplicated(command);
    }

    private  CheckDuplicationResponse toResponse(boolean duplicated) {
        return new CheckDuplicationResponse(duplicated);
    }

    public record CheckDuplicationResponse(boolean duplicated) {}
}
