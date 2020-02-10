package com.github.marekp95.rest.example.api.controller;

import com.github.marekp95.rest.example.api.request.TransactionRequest;
import com.github.marekp95.rest.example.api.service.TransactionService;
import com.github.marekp95.rest.example.model.Account;
import com.github.marekp95.rest.example.model.Transaction;
import com.github.marekp95.rest.example.storage.repository.AccountRepository;
import com.github.marekp95.rest.example.storage.repository.TransactionRepository;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.Param;
import restx.annotations.RestxResource;
import restx.exceptions.ErrorCode;
import restx.exceptions.RestxErrors;
import restx.factory.Component;
import restx.http.HttpStatus;
import restx.security.PermitAll;

import java.util.Collection;
import java.util.UUID;

@Component
@RestxResource("/transaction")
public class TransactionController {

    private final RestxErrors errors;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public TransactionController(RestxErrors errors, AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionService transactionService) {
        this.errors = errors;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    @POST("")
    @PermitAll
    public void orderTransaction(TransactionRequest transactionRequest) {
        if (!transactionRequest.isValid()) {
            throw errors.on(Rules.NotFound.class).raise();
        }
        final Account sender = accountRepository.find(transactionRequest.getSenderId())
                .orElseThrow(() -> errors.on(Rules.NotFound.class).raise());
        final Account recipient = accountRepository.find(transactionRequest.getRecipientId())
                .orElseThrow(() -> errors.on(Rules.NotFound.class).raise());
        transactionService.processTransaction(sender, recipient, transactionRequest.getAmount());
    }

    @GET("")
    @PermitAll
    public Collection<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GET("/{id}")
    @PermitAll
    public Transaction getTransactionData(@Param(value = "id", kind = Param.Kind.PATH) UUID id) {
        return transactionRepository.find(id).orElseThrow(() -> errors.on(Rules.NotFound.class).raise());
    }

    @GET("/account/{id}")
    @PermitAll
    public Collection<Transaction> getAccountRelatedTransactions(@Param(value = "id", kind = Param.Kind.PATH) UUID id) {
        final Account account = accountRepository.find(id)
                .orElseThrow(() -> errors.on(Rules.NotFound.class).raise());
        return transactionService.findAllRelatedTransactions(account);
    }

    public static class Rules {
        @ErrorCode(code = "", description = "", status = HttpStatus.I_AM_A_TEAPOT)
        public static class NotFound {
        }
    }
}
