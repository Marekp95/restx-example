package com.github.marekp95.rest.example.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {

    private final Instant timestamp;
    private final Account sender;
    private final Account recipient;
    private final BigDecimal amount;

    public Transaction(Instant timestamp, Account sender, Account recipient, BigDecimal amount) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Account getSender() {
        return sender;
    }

    public Account getRecipient() {
        return recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
