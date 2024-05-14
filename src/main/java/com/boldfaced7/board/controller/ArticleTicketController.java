package com.boldfaced7.board.controller;

import com.boldfaced7.board.dto.ArticleTicketDto;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.ArticleTicketListResponse;
import com.boldfaced7.board.dto.response.ArticleTicketResponse;
import com.boldfaced7.board.service.ArticleTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ArticleTicketController {

    private final ArticleTicketService articleTicketService;

    @GetMapping("/articleTickets/{articleTicketId}")
    public ResponseEntity<ArticleTicketResponse> getArticleTicket(
            @PathVariable long articleTicketId) {

        ArticleTicketDto request = new ArticleTicketDto(articleTicketId);
        ArticleTicketDto articleTicket = articleTicketService.getIssuedTicket(request);
        ArticleTicketResponse response = new ArticleTicketResponse(articleTicket);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articleTickets")
    public ResponseEntity<ArticleTicketListResponse> getArticleTickets(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        CustomPage<ArticleTicketDto> articleTickets = articleTicketService.getIssuedTickets(pageable, date);
        ArticleTicketListResponse response = new ArticleTicketListResponse(articleTickets);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/members/{memberId}/articleTickets")
    public ResponseEntity<ArticleTicketListResponse> getArticleTickets(
            @PathVariable long memberId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        MemberDto request = new MemberDto(memberId, pageable);
        CustomPage<ArticleTicketDto> articleTickets = articleTicketService.getIssuedTickets(request);
        ArticleTicketListResponse response = new ArticleTicketListResponse(articleTickets);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/articleTickets")
    public ResponseEntity<Void> issueTicket() {
        long articleTicketId = articleTicketService.issueTicket();
        return ResponseEntity.created(URI.create("/api/articleTickets/" + articleTicketId)).build();
    }
}
