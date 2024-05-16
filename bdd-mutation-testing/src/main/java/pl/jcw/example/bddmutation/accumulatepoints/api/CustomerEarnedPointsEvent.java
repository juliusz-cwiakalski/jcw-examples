package pl.jcw.example.bddmutation.accumulatepoints.api;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record CustomerEarnedPointsEvent(
    String transactionId,
    Instant transactionTimestamp,
    String customerId,
    BigDecimal points,
    Instant tierValidityDate,
    Instant redemptionValidityDate,
    String description) {}
