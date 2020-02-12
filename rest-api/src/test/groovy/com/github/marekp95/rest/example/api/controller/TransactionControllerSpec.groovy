package com.github.marekp95.rest.example.api.controller

import com.github.marekp95.rest.example.api.request.TransactionRequest
import com.github.marekp95.rest.example.api.service.TransactionService
import com.github.marekp95.rest.example.model.Account
import com.github.marekp95.rest.example.model.Transaction
import com.github.marekp95.rest.example.storage.repository.AccountRepository
import com.github.marekp95.rest.example.storage.repository.TransactionRepository
import restx.exceptions.RestxError
import restx.exceptions.RestxErrors
import spock.lang.Specification
import spock.lang.Unroll

class TransactionControllerSpec extends Specification {

    def "throws exception when transaction is ordered with invalid parameters"() {
        given: 'a transaction controller'
        TransactionController transactionController = createTransactionController()

        when: 'a new transaction is ordered'
        transactionController.orderTransaction(Mock(TransactionRequest) {
            isValid() >> false
        })

        then: 'an exception will be thrown'
        thrown(RuntimeException)
    }

    def "throws exception when transaction is ordered with non existing sender id"() {
        given: 'a recipient'
        Account recipient = new Account('firstName', 'lastName', 10.0)
        and: 'an account repository'
        AccountRepository accountRepository = createAccountRepositoryMock([recipient])
        'a transaction controller'
        TransactionController transactionController = createTransactionController(accountRepository)

        when: 'a new transaction is ordered'
        transactionController.orderTransaction(new TransactionRequest(UUID.randomUUID(), recipient.accountId, 0.0))

        then: 'an exception will be thrown'
        thrown(RuntimeException)
    }

    def "throws exception when transaction is ordered with non existing recipient id"() {
        given: 'a sender'
        Account sender = new Account('firstName', 'lastName', 10.0)
        and: 'an account repository'
        AccountRepository accountRepository = createAccountRepositoryMock([sender])
        'a transaction controller'
        TransactionController transactionController = createTransactionController(accountRepository)

        when: 'a new transaction is ordered'
        transactionController.orderTransaction(new TransactionRequest(sender.accountId, UUID.randomUUID(), 0.0))

        then: 'an exception will be thrown'
        thrown(RuntimeException)
    }

    def "do not throw exception when transaction is ordered with a proper parameters"() {
        given: 'a sender'
        Account sender = new Account('firstName', 'lastName', 10.0)
        and: 'a recipient'
        Account recipient = new Account('firstName', 'lastName', 10.0)
        and: 'an account repository'
        AccountRepository accountRepository = createAccountRepositoryMock([sender, recipient])
        'a transaction controller'
        TransactionController transactionController = createTransactionController(accountRepository)

        when: 'a new transaction is ordered'
        transactionController.orderTransaction(new TransactionRequest(sender.accountId, recipient.accountId, 0.0))

        then: 'no exception will be thrown'
        noExceptionThrown()
    }

    @Unroll
    def "returns all transactions from the repository"() {
        given: 'a transaction repository'
        TransactionRepository transactionRepository = Mock(TransactionRepository) {
            findAll() >> transactions
        }
        and: 'a transaction controller'
        TransactionController transactionController = createTransactionController(createAccountRepositoryMock(), transactionRepository)

        expect: 'the controller will return the same transactions as the repository'
        transactionController.getAllTransactions() == transactions

        where:
        transactions                                              | _
        []                                                        | _
        [Mock(Transaction), Mock(Transaction), Mock(Transaction)] | _
    }

    def "throws exception when there is no transaction with the specified id inside repository"() {
        given: 'a transaction repository'
        TransactionRepository transactionRepository = Mock(TransactionRepository) {
            find(_ as UUID) >> Optional.empty()
        }
        and: 'a transaction controller'
        TransactionController transactionController = createTransactionController(createAccountRepositoryMock(), transactionRepository)

        when: 'the controller is called for a non existing transaction data'
        transactionController.getTransactionData(UUID.randomUUID())

        then: 'an exception will be thrown'
        thrown(RuntimeException)
    }

    def "returns transaction with the specified id"() {
        given: 'a transaction id'
        UUID transactionId = UUID.randomUUID()
        and: 'a transaction repository'
        TransactionRepository transactionRepository = Mock(TransactionRepository) {
            find(transactionId) >> Optional.of(Mock(Transaction) {
                getId() >> transactionId
            })
            find(_ as UUID) >> Optional.empty()
        }
        and: 'a transaction controller'
        TransactionController transactionController = createTransactionController(createAccountRepositoryMock(), transactionRepository)

        expect: 'the controller will return transaction with the specified id'
        transactionController.getTransactionData(transactionId).id == transactionId
    }

    def "returns account related transactions"() {
        given: 'an account'
        Account account = new Account('firstName', 'lastName')
        and: 'a list of account related transactions'
        List<Transaction> transactions = [
                new Transaction(account, new Account('firstName', 'lastName'), 1.0),
                new Transaction(new Account('firstName', 'lastName'), account, 1.0),
        ]
        and: 'an account repository'
        AccountRepository accountRepository = createAccountRepositoryMock([account])
        and: 'a transaction service'
        TransactionService transactionService = createTransactionServiceMock(account, transactions)
        and: 'a transaction controller'
        TransactionController transactionController = createTransactionController(accountRepository, createTransactionRepositoryMock(), transactionService)

        expect: 'all account related transactions will be found'
        transactionController.getAccountRelatedTransactions(account.accountId) == transactions
    }

    def "returns empty list of account related transactions when there is no such transaction in repository"() {
        given: 'an account'
        Account account = new Account('firstName', 'lastName')
        and: 'an account repository'
        AccountRepository accountRepository = createAccountRepositoryMock([account])
        and: 'a transaction controller'
        TransactionController transactionController = createTransactionController(accountRepository, createTransactionRepositoryMock())

        expect: 'none transaction will be found'
        transactionController.getAccountRelatedTransactions(account.accountId) == []
    }

    private TransactionController createTransactionController(
            AccountRepository accountRepository = createAccountRepositoryMock(),
            TransactionRepository repository = createTransactionRepositoryMock(),
            TransactionService transactionService = createTransactionServiceMock()) {
        RestxErrors restxErrors = Mock(RestxErrors) {
            on(_ as Class) >> Mock(RestxError) {
                raise() >> new RuntimeException()
            }
        }
        new TransactionController(restxErrors, accountRepository, repository, transactionService)
    }

    private AccountRepository createAccountRepositoryMock(List<Account> accounts = []) {
        AccountRepository accountRepository = Mock(AccountRepository) {
            findAll() >> accounts
        }
        accounts.each {
            accountRepository.find(it.accountId) >> Optional.of(it)
        }
        accountRepository.find(_ as UUID) >> Optional.empty()
        accountRepository
    }

    private TransactionRepository createTransactionRepositoryMock() {
        Mock(TransactionRepository)
    }

    private TransactionService createTransactionServiceMock(Account account = null, List<Transaction> relatedTransactions = []) {
        Mock(TransactionService) {
            findAllRelatedTransactions(account) >> relatedTransactions
            findAllRelatedTransactions(_ as Account) >> []
        }
    }
}
