package com.github.marekp95.rest.example.api.request;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequest {

    private UUID senderId;
    private UUID recipientId;
    private BigDecimal amount;

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
    public String toString() {
        return Objects.toStringHelper(this)
                .add("senderId", senderId)
                .add("recipientId", recipientId)
                .add("amount", amount)
                .toString();
    }
}
