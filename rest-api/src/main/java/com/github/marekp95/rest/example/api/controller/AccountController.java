package com.github.marekp95.rest.example.api.controller;

import com.github.marekp95.rest.example.api.request.AccountRequest;
import com.github.marekp95.rest.example.model.Account;
import com.github.marekp95.rest.example.storage.repository.AccountRepository;
import restx.annotations.DELETE;
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
@RestxResource("/account")
public class AccountController {

    private final RestxErrors errors;
    private final AccountRepository accountRepository;

    public AccountController(RestxErrors errors, AccountRepository accountRepository) {
        this.errors = errors;
        this.accountRepository = accountRepository;
    }

    @POST("")
    @PermitAll
    // We intent this method to be invoked with some authority, such as, e.g., an authorized bank employee.
    public void createAccount(AccountRequest accountRequest) {
        if (!accountRequest.isValid()) {
            throw errors.on(Rules.UnableToCreateAccount.class).raise();
        }
        accountRepository.insert(new Account(accountRequest.getFirstName(), accountRequest.getLastName(), accountRequest.getInitialBalance()));
    }

    @GET("")
    @PermitAll
    public Collection<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GET("/{id}")
    @PermitAll
    public Account getAccountData(@Param(value = "id", kind = Param.Kind.PATH) UUID id) {
        return accountRepository.find(id).orElseThrow(() -> errors.on(Rules.AccountNotFound.class).raise());
    }

    @DELETE("/{id}")
    @PermitAll
    public void deleteAccount(@Param(value = "id", kind = Param.Kind.PATH) UUID id) {
        accountRepository.remove(id);
    }

    private static class Rules {
        @ErrorCode(code = "ANF", description = "Account not found.", status = HttpStatus.BAD_REQUEST)
        public static class AccountNotFound {
        }

        @ErrorCode(code = "UTCA", description = "Unable to create account - invalid parameters.", status = HttpStatus.BAD_REQUEST)
        public static class UnableToCreateAccount {
        }
    }
}
