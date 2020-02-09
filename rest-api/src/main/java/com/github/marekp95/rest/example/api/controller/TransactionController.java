package com.github.marekp95.rest.example.api.controller;

import com.github.marekp95.rest.example.api.request.TransactionRequest;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

@Component
@RestxResource("/transaction")
public class TransactionController {

    @POST("")
    @PermitAll
    public void orderTransaction(TransactionRequest transactionRequest) {
        System.out.println(transactionRequest);
    }
}
