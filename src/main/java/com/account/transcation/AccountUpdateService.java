package com.account.transcation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountUpdateService {

    private final AccountRepository accountRepository;

    @Transactional
    public long deposit(final long accountId, final long amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(accountId + "는 존재하지 않는 계좌입니다."));

        account.addBalance(amount); // 계좌 입금 처리

        return account.getBalance();
    }

}
