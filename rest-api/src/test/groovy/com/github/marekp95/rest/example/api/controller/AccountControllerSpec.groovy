package com.github.marekp95.rest.example.api.controller

import com.github.marekp95.rest.example.api.request.AccountRequest
import com.github.marekp95.rest.example.model.Account
import com.github.marekp95.rest.example.storage.repository.AccountRepository
import restx.exceptions.RestxError
import restx.exceptions.RestxErrors
import spock.lang.Specification
import spock.lang.Unroll

class AccountControllerSpec extends Specification {

    def "throws exception when account is created with invalid parameters"() {
        given: 'an account controller'
        AccountController accountController = createAccountController()

        when: 'a new account is created'
        accountController.createAccount(Mock(AccountRequest) {
            isValid() >> false
        })

        then: 'an exception will be thrown'
        thrown(RuntimeException)
    }

    def "do not throw exception when account is created with a proper parameters"() {
        given: 'an account controller'
        AccountController accountController = createAccountController()

        when: 'a new account is created'
        accountController.createAccount(new AccountRequest('firstName', 'lastName', 0.0))

        then: 'no exception will be thrown'
        noExceptionThrown()
    }

    @Unroll
    def "returns all accounts from the repository"() {
        given: 'an account repository'
        AccountRepository accountRepository = Mock(AccountRepository) {
            findAll() >> accounts
        }
        and: 'an account controller'
        AccountController accountController = createAccountController(accountRepository)

        expect: 'the controller will return the same accounts as the repository'
        accountController.getAllAccounts() == accounts

        where:
        accounts                                      | _
        []                                            | _
        [Mock(Account), Mock(Account), Mock(Account)] | _
    }

    def "throws exception when there is no account with the specified id inside repository"() {
        given: 'an account repository'
        AccountRepository accountRepository = Mock(AccountRepository) {
            find(_ as UUID) >> Optional.empty()
        }
        and: 'an account controller'
        AccountController accountController = createAccountController(accountRepository)

        when: 'the controller is called for a non existing account data'
        accountController.getAccountData(UUID.randomUUID())

        then: 'an exception will be thrown'
        thrown(RuntimeException)
    }

    def "returns account with the specified id"() {
        given: 'an account id'
        UUID accountId = UUID.randomUUID()
        and: 'an account repository'
        AccountRepository accountRepository = Mock(AccountRepository) {
            find(accountId) >> Optional.of(Mock(Account) {
                getAccountId() >> accountId
            })
            find(_ as UUID) >> Optional.empty()
        }
        and: 'an account controller'
        AccountController accountController = createAccountController(accountRepository)

        expect: 'the controller will return account with the specified id'
        accountController.getAccountData(accountId).accountId == accountId
    }

    def "removes an account with the specified id from the repository"() {
        given: 'an account id'
        UUID accountId = UUID.randomUUID()
        and: 'an account repository'
        AccountRepository accountRepository = Mock(AccountRepository)
        and: 'an account controller'
        AccountController accountController = createAccountController(accountRepository)

        when: 'the controller is called to remove account'
        accountController.deleteAccount(accountId)

        then: 'the account will be removed from the repository'
        1 * accountRepository.remove(accountId)
    }

    private AccountController createAccountController(AccountRepository repository = createAccountRepositoryMock()) {
        RestxErrors restxErrors = Mock(RestxErrors) {
            on(_ as Class) >> Mock(RestxError) {
                raise() >> new RuntimeException()
            }
        }
        new AccountController(restxErrors, repository)
    }

    private AccountRepository createAccountRepositoryMock() {
        Mock(AccountRepository)
    }
}
