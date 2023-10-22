package pl.jcw.example.bddmutation.account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.Repository;

interface AccountRepository extends Repository<Account, UUID> {
  Account save(Account account);

  Optional<Account> findById(UUID accountId);
}

class InMemoryAccountRepository implements AccountRepository {
  private final Map<UUID, Account> accounts = new HashMap<>();

  @Override
  public Account save(Account account) {
    accounts.put(account.getAccountId(), account);
    return account;
  }

  @Override
  public Optional<Account> findById(UUID accountId) {
    return Optional.ofNullable(accounts.get(accountId));
  }
}
