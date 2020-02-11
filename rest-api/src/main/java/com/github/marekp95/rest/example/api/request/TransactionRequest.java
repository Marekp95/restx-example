package com.github.marekp95.rest.example.api.request;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequest implements Request {

    private UUID senderId;
    private UUID recipientId;
    private BigDecimal amount;

    public TransactionRequest() {
    }

    public TransactionRequest(UUID senderId, UUID recipientId, BigDecimal amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean isValid() {
        return senderId != null
                && recipientId != null
                && amount != null && amount.signum() >= 0 && amount.scale() <= 2;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("senderId", senderId)
                .add("recipientId", recipientId)
                .add("amount", amount)
                .toString();
    }
}
