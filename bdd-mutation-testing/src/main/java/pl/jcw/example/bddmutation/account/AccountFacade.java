package pl.jcw.example.bddmutation.account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import pl.jcw.example.bddmutation.account.api.AccountDto;

public class AccountFacade {

  private final AccountRepository accountRepository;

  public AccountFacade(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  AccountDto createAccount(UUID userId, String accountName) {
    return this.accountRepository.save(new Account(userId, accountName)).toDto();
  }

  void withdraw(UUID accountId, BigDecimal amount) {
    this.accountRepository
        .findById(accountId)
        .ifPresent(
            account -> {
              account.withdraw(amount);
              this.accountRepository.save(account);
            });
  }

  void deposit(UUID accountId, BigDecimal amount) {
    this.accountRepository
        .findById(accountId)
        .ifPresent(
            account -> {
              account.deposit(amount);
              this.accountRepository.save(account);
            });
  }

  BigDecimal getBalance(UUID accountId) {
    Optional<Account> account = accountRepository.findById(accountId);
    return account.map(Account::getBalance).orElseThrow();
  }
}
