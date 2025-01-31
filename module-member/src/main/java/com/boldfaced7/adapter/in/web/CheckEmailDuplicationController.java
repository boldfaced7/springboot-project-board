package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.CheckEmailDuplicationCommand;
import com.boldfaced7.application.port.in.CheckEmailDuplicationQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class CheckEmailDuplicationController {

    private final CheckEmailDuplicationQuery checkEmailDuplicationQuery;

    @GetMapping("/members/check-email")
    public ResponseEntity<CheckDuplicationResponse> checkEmailDuplication(
            @RequestParam String email
    ) {
        boolean duplicated = isDuplicated(email);
        CheckDuplicationResponse response = totResponse(duplicated);
        return ResponseEntity.ok(response);
    }

    private boolean isDuplicated(String email) {
        CheckEmailDuplicationCommand command = new CheckEmailDuplicationCommand(email);
        return checkEmailDuplicationQuery.isDuplicated(command);
    }

    private CheckDuplicationResponse totResponse(boolean duplicated) {
        return new CheckDuplicationResponse(duplicated);
    }

    public record CheckDuplicationResponse(boolean duplicated) {}
}