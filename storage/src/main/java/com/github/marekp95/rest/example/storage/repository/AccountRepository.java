package com.github.marekp95.rest.example.storage.repository;

import com.github.marekp95.rest.example.model.Account;

public class AccountRepository extends Repository<Account> {

    public void insert(Account account) {
        insert(account.getAccountId(), account);
    }
}
