package com.github.marekp95.rest.example.model;

import java.util.UUID;

public class Account {

    private final UUID accountId;
    private final String firstName;
    private final String lastName;

    public Account(UUID accountId, String firstName, String lastName) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
