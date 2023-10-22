package pl.jcw.example.bddmutation.account

import pl.jcw.example.bddmutation.account.api.AccountDto
import spock.lang.Specification

class AccountSpec extends Specification{
  UUID userId = UUID.randomUUID()
  AccountFacade accountFacade = new AccountConfiguration().inMemoryAccountFacade()
  AccountDto account = accountFacade.createAccount(userId, "Test account")

  def "given account with deposit 100 when withdraw 40 then balance is 60"() {
    given: "deposit 100"
    accountFacade.deposit(account.accountId(), BigDecimal.valueOf(100))

    when: "withdraw 40"
    accountFacade.withdraw(account.accountId(), BigDecimal.valueOf(40))

    then: "balance is 60"
    accountFacade.getBalance(account.accountId()) != null
  }
}
