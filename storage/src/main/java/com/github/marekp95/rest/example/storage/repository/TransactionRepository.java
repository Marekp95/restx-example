package com.github.marekp95.rest.example.storage.repository;

import com.github.marekp95.rest.example.model.Transaction;

public class TransactionRepository extends Repository<Transaction> {

    public void insert(Transaction transaction) {
        insert(transaction.getId(), transaction);
    }
}
