package pl.jcw.example.bddmutation.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;
import pl.jcw.example.bddmutation.account.api.AccountDto;

@Entity
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Account {

  Account(UUID userId, String accountName) {
    this.accountId = UUID.randomUUID();
    this.userId = userId;
    this.name = accountName;
    this.balance = BigDecimal.ZERO;
  }

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
  @Getter
  private UUID accountId;

  private UUID userId;
  private String name;
  @Getter private BigDecimal balance;

  AccountDto toDto() {
    return new AccountDto(accountId, userId, name);
  }

  public void withdraw(BigDecimal amount) {
    this.balance = this.balance.subtract(amount);
  }

  public void deposit(BigDecimal amount) {
    this.balance = this.balance.add(amount);
  }
}
