package com.github.marekp95.rest.example.api.request;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.math.BigDecimal;

public class AccountRequest implements Request {

    private String firstName;
    private String lastName;
    private BigDecimal initialBalance;

    public AccountRequest() {
    }

    public AccountRequest(String firstName, String lastName, BigDecimal initialBalance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.initialBalance = initialBalance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(firstName)
                && !Strings.isNullOrEmpty(lastName)
                && initialBalance != null && initialBalance.signum() >= 0 && initialBalance.scale() <= 2;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .toString();
    }
}
