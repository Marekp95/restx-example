package com.github.marekp95.rest.example.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final Instant timestamp;
    private final Account sender;
    private final Account recipient;
    private final BigDecimal amount;

    public Transaction(Account sender, Account recipient, BigDecimal amount) {
        this(UUID.randomUUID(), Instant.now(), sender, recipient, amount);
    }

    public Transaction(UUID id, Instant timestamp, Account sender, Account recipient, BigDecimal amount) {
        this.id = id;
        this.timestamp = timestamp;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(sender, that.sender)
                && Objects.equals(recipient, that.recipient)
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, sender, recipient, amount);
    }
}
