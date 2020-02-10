package com.github.marekp95.rest.example.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private final UUID accountId;
    private final String firstName;
    private final String lastName;
    private BigDecimal balance;

    public Account(String firstName, String lastName) {
        this(UUID.randomUUID(), firstName, lastName, BigDecimal.ZERO);
    }

    public Account(UUID accountId, String firstName, String lastName, BigDecimal balance) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addMoney(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void takeMoney(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId)
                && Objects.equals(firstName, account.firstName)
                && Objects.equals(lastName, account.lastName)
                && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, firstName, lastName, balance);
    }
}
