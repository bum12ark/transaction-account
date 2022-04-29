package com.account.transcation;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private long balance = 0;

    public static Account of(String name) {
        Account account = new Account();
        account.name = name;
        return account;
    }

    public void addBalance(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("0원 이하는 입금 불가능합니다.");
        this.balance += amount;
    }

    public void setName(String name) {
        this.name = name;
    }
}
