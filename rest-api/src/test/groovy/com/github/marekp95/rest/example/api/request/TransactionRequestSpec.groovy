package com.github.marekp95.rest.example.api.request

import spock.lang.Specification
import spock.lang.Unroll

class TransactionRequestSpec extends Specification {

    def "validates transaction request with missing parameters"() {
        given: 'an transaction request with missing parameters'
        TransactionRequest transactionRequest = new TransactionRequest(senderId, recipientId, amount)

        expect: 'the transaction request will be invalid'
        !transactionRequest.valid

        where:
        senderId          | recipientId       | amount
        null              | null              | null
        UUID.randomUUID() | UUID.randomUUID() | null
        UUID.randomUUID() | null              | 0.0
        null              | UUID.randomUUID() | 0.0
        UUID.randomUUID() | null              | null
        null              | UUID.randomUUID() | null
        null              | null              | 0.0
    }

    def "validates transaction request with amount with too much decimal precision"() {
        given: 'an transaction request with amount with too much decimal precision'
        TransactionRequest transactionRequest = new TransactionRequest(UUID.randomUUID(), UUID.randomUUID(), amount)

        expect: 'the transaction request will be invalid'
        !transactionRequest.valid

        where:
        amount | _
        0.001  | _
        1e-10  | _
    }

    @Unroll
    def "validates transaction request with a negative amount"() {
        given: 'an transaction request with a negative amount'
        TransactionRequest transactionRequest = new TransactionRequest(UUID.randomUUID(), UUID.randomUUID(), amount)

        expect: 'the transaction request will be invalid'
        !transactionRequest.valid

        where:
        amount | _
        -0.01  | _
        -1e10  | _
    }

    def "validates transaction request with a proper parameters"() {
        given: 'an transaction request with a proper parameters'
        TransactionRequest transactionRequest = new TransactionRequest(UUID.randomUUID(), UUID.randomUUID(), 0.0)

        expect: 'the transaction request will be valid'
        transactionRequest.valid
    }
}
