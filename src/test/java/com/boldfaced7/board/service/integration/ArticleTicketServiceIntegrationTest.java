package com.boldfaced7.board.service.integration;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.repository.ArticleTicketRepository;
import com.boldfaced7.board.service.ArticleTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.boldfaced7.board.TestUtil.authResponse;

@ActiveProfiles("test")
@DisplayName("ArticleTicketService 통합 테스트")
@SpringBootTest
public class ArticleTicketServiceIntegrationTest {
    @Autowired ArticleTicketService articleTicketService;
    @Autowired ArticleTicketRepository articleTicketRepository;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(authResponse());
    }

    @Test
    void issueTicketTest() throws InterruptedException {
        int memberCount = 200;

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(memberCount);
        CountDownLatch latch = new CountDownLatch(memberCount);

        for (int i = 0; i < memberCount; i++) {
            executorService.submit(() -> {
               try {
                   AuthInfoHolder.setAuthInfo(authResponse());
                   articleTicketService.issueTicket();
                   successCount.incrementAndGet();
               } catch (Exception e) {
                   System.out.println(e.getMessage());
                   failCount.incrementAndGet();
               } finally {
                   latch.countDown();
               }
            });
        }
        latch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);
    }

}
