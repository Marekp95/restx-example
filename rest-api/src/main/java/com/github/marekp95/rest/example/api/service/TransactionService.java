package com.github.marekp95.rest.example.api.service;

import com.github.marekp95.rest.example.model.Account;
import com.github.marekp95.rest.example.model.Transaction;
import com.github.marekp95.rest.example.storage.repository.TransactionRepository;
import restx.exceptions.ErrorCode;
import restx.exceptions.RestxErrors;
import restx.factory.Component;
import restx.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class TransactionService {

    private final RestxErrors errors;
    private final TransactionRepository transactionRepository;

    public TransactionService(RestxErrors errors, TransactionRepository transactionRepository) {
        this.errors = errors;
        this.transactionRepository = transactionRepository;
    }

    public Collection<Transaction> findAllRelatedTransactions(Account account) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getSender().equals(account) || transaction.getRecipient().equals(account))
                .collect(Collectors.toList());
    }

    public void processTransaction(Account sender, Account recipient, BigDecimal amount) {
        if (!hasEnoughMoney(sender, amount)) {
            throw errors.on(Rules.NotFound.class).raise();
        }
        sender.takeMoney(amount);
        recipient.addMoney(amount);
        transactionRepository.insert(new Transaction(sender, recipient, amount));
    }

    private boolean hasEnoughMoney(Account sender, BigDecimal amount) {
        return sender.getBalance().subtract(amount).signum() >= 0;
    }

    public static class Rules {
        @ErrorCode(code = "", description = "", status = HttpStatus.I_AM_A_TEAPOT)
        public static class NotFound {
        }
    }
}
