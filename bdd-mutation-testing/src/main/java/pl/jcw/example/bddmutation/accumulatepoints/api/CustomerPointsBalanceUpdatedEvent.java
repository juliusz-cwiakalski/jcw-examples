package pl.jcw.example.bddmutation.accumulatepoints.api;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record CustomerPointsBalanceUpdatedEvent(
    Instant balanceTimestamp,
    String customerId,
    BigDecimal tierPointsBalance,
    Instant tierValidityDate) {}
