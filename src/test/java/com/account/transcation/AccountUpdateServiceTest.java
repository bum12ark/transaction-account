package com.account.transcation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class AccountUpdateServiceTest {

    private static final ExecutorService service = Executors.newFixedThreadPool(100);

    @Autowired AccountUpdateService accountUpdateService;

    @Autowired AccountRepository accountRepository;

    private long accountId;

    @BeforeEach
    void beforeAll() {
        Account account = Account.of("우리");
        accountRepository.save(account);
        accountId = account.getId();
    }

    @Test
    @DisplayName("입금 테스트")
    void depositSimpleTest() {
        // given
        int amount = 10;

        // when
        accountUpdateService.deposit(accountId, amount);
        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(findAccount.getBalance()).isEqualTo(amount);
    }

    @Test
    @DisplayName("입금 멀티 스레드 race condition 테스트")
    void depositMultiThreadRaceConditionTest() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // given
        int count = 100;

        // then
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            service.execute(() -> {
                accountUpdateService.deposit(accountId, 10);
                latch.countDown();
            });
        }
        latch.await();

        // when
        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(IllegalArgumentException::new);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        assertThat(findAccount.getBalance()).isEqualTo(10 * 100);

    }

}