package com.github.marekp95.rest.example.api.service

import com.github.marekp95.rest.example.model.Account
import com.github.marekp95.rest.example.model.Transaction
import com.github.marekp95.rest.example.storage.repository.TransactionRepository
import restx.exceptions.RestxError
import restx.exceptions.RestxErrors
import spock.lang.Specification

class TransactionServiceSpec extends Specification {

    def "do not allow to process transaction when the sender has not enough money"() {
        given: 'a sender'
        Account sender = new Account('firstName', 'lastName', 10.0)
        and: 'a recipient'
        Account recipient = new Account('firstName', 'lastName')
        and: 'a transaction service'
        TransactionService transactionService = createTransactionService()

        when: 'a transaction involving more money than the sender has is processed'
        transactionService.processTransaction(sender, recipient, 100.0)

        then: 'an exception will be thrown'
        thrown(RuntimeException)
    }

    def "process transaction when the sender has enough money"() {
        given: 'a sender'
        Account sender = new Account('firstName', 'lastName', 10.0)
        and: 'a recipient'
        Account recipient = new Account('firstName', 'lastName', 0.0)
        and: 'a transaction repository'
        TransactionRepository transactionRepository = createTransactionRepositoryMock()
        and: 'a transaction service'
        TransactionService transactionService = createTransactionService(transactionRepository)

        when: 'a transaction is processed'
        transactionService.processTransaction(sender, recipient, 10.0)

        then: 'specified amount of money will be taken from the sender'
        sender.balance == 0.0
        and: 'specified amount of money will be given to the recipient'
        recipient.balance == 10.0
        and: 'the transaction will be added to the repository'
        1 * transactionRepository.insert(_ as Transaction)
    }

    def "finds all account-related transactions"() {
        given: 'an account'
        Account account = new Account('firstName', 'lastName')
        and: 'a list of account related transactions'
        List<Transaction> accountRelatedTransactions = [
                new Transaction(account, new Account('firstName', 'lastName'), 1.0),
                new Transaction(new Account('firstName', 'lastName'), account, 1.0),
        ]
        and: 'a list of account not-related transactions'
        List<Transaction> accountNotRelatedTransactions = [
                new Transaction(new Account('firstName', 'lastName'), new Account('firstName', 'lastName'), 1.0),
        ]
        and: 'a transaction repository'
        TransactionRepository transactionRepository = createTransactionRepositoryMock(accountRelatedTransactions + accountNotRelatedTransactions)
        and: 'a transaction service'
        TransactionService transactionService = createTransactionService(transactionRepository)

        expect: 'all account-related transactions will be found'
        transactionService.findAllRelatedTransactions(account) == accountRelatedTransactions
    }

    private TransactionService createTransactionService(TransactionRepository repository = createTransactionRepositoryMock()) {
        RestxErrors restxErrors = Mock(RestxErrors) {
            on(_ as Class) >> Mock(RestxError) {
                raise() >> new RuntimeException()
            }
        }
        new TransactionService(restxErrors, repository)
    }

    private TransactionRepository createTransactionRepositoryMock(List<Transaction> transactions) {
        Mock(TransactionRepository) {
            findAll() >> transactions
        }
    }
}
