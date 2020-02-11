package com.github.marekp95.rest.example.api.request

import spock.lang.Specification
import spock.lang.Unroll

class AccountRequestSpec extends Specification {

    def "validates account request with missing parameters"() {
        given: 'an account request with missing parameters'
        AccountRequest accountRequest = new AccountRequest(firstName, lastName, balance)

        expect: 'the account request will be invalid'
        !accountRequest.valid

        where:
        firstName   | lastName   | balance
        null        | null       | null
        'firstName' | 'lastName' | null
        'firstName' | null       | 0.0
        null        | 'lastName' | 0.0
        'firstName' | null       | null
        null        | 'lastName' | null
        null        | null       | 0.0
    }

    def "validates account request with balance with too much decimal precision"() {
        given: 'an account request with balance with too much decimal precision'
        AccountRequest accountRequest = new AccountRequest('firstName', 'lastName', balance)

        expect: 'the account request will be invalid'
        !accountRequest.valid

        where:
        balance | _
        0.001   | _
        1e-10   | _
    }

    @Unroll
    def "validates account request with a negative balance"() {
        given: 'an account request with a negative balance'
        AccountRequest accountRequest = new AccountRequest('firstName', 'lastName', balance)

        expect: 'the account request will be invalid'
        !accountRequest.valid

        where:
        balance | _
        -0.01   | _
        -1e10   | _
    }

    def "validates account request with a proper parameters"() {
        given: 'an account request with a proper parameters'
        AccountRequest accountRequest = new AccountRequest('firstName', 'lastName', 0.0)

        expect: 'the account request will be valid'
        accountRequest.valid
    }
}
