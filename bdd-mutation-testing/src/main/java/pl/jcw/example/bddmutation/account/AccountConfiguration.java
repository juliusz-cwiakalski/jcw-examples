package pl.jcw.example.bddmutation.account;

import org.springframework.context.annotation.Bean;

class AccountConfiguration {
  @Bean
  AccountFacade accountFacade(AccountRepository accountRepository) {
    return new AccountFacade(accountRepository);
  }

  AccountFacade inMemoryAccountFacade() {
    return accountFacade(new InMemoryAccountRepository());
  }
}
