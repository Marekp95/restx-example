package com.github.marekp95.rest.example.api;

import com.github.marekp95.rest.example.storage.repository.AccountRepository;
import com.github.marekp95.rest.example.storage.repository.TransactionRepository;
import restx.factory.Module;
import restx.factory.Provides;

@Module
public class AppModule {

    @Provides
    public AccountRepository accountRepository() {
        return new AccountRepository();
    }

    @Provides
    public TransactionRepository transactionRepository() {
        return new TransactionRepository();
    }
}