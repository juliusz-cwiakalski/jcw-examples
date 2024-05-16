package pl.jcw.example.bddmutation.accumulatepoints.api;

import java.math.BigDecimal;
import java.time.Duration;
import lombok.Builder;

@Builder
public record PointsEarned(
    String customerId,
    BigDecimal points,
    Duration tierValidity,
    Duration redemptionValidity,
    String description) {}
