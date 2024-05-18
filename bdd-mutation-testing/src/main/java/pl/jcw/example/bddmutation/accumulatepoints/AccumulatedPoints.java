package pl.jcw.example.bddmutation.accumulatepoints;

import com.github.ksuid.Ksuid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Getter;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent;
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned;

@Entity
class AccumulatedPoints {

  protected AccumulatedPoints() {}

  AccumulatedPoints(PointsEarned pointsEarned, Instant now) {
    this.transactionId = Ksuid.newKsuid().toString();
    this.transactionTimestamp = now;
    this.customerId = pointsEarned.customerId();
    this.points = pointsEarned.points();
    this.tierValidityDate = now.plus(pointsEarned.tierValidity().toDays(), ChronoUnit.DAYS);
    this.redemptionValidityDate =
        now.plus(pointsEarned.redemptionValidity().toDays(), ChronoUnit.DAYS);
    this.description = pointsEarned.description();
  }

  @Id
  @Column(nullable = false, unique = true, updatable = false)
  @Getter
  private String transactionId;

  @Column(nullable = false, updatable = false)
  private Instant transactionTimestamp;

  @Column(nullable = false, updatable = false)
  private String customerId;

  @Column(nullable = false, updatable = false)
  @Getter
  private BigDecimal points;

  @Column(nullable = false, updatable = false)
  @Getter
  private Instant tierValidityDate;

  @Column(nullable = false, updatable = false)
  private Instant redemptionValidityDate;

  @Column(updatable = false)
  private String description;

  static Predicate<AccumulatedPoints> customerIdIs(String customerId) {
    return points -> Objects.equals(points.customerId, customerId);
  }

  static Predicate<AccumulatedPoints> earnedBetween(Instant from, Instant to) {
    return isBetween(points -> points.transactionTimestamp, from, to);
  }

  static Predicate<AccumulatedPoints> isTierValid(Instant now) {
    return isAfter(points -> points.tierValidityDate, now);
  }

  static Predicate<AccumulatedPoints> isBetween(
      Function<AccumulatedPoints, Instant> field, Instant from, Instant to) {
    return isAfter(field, from).and(isBefore(field, to));
  }

  static Predicate<AccumulatedPoints> isAfter(
      Function<AccumulatedPoints, Instant> field, Instant timestamp) {
    return points ->
        (field.apply(points).isAfter(timestamp) || field.apply(points).equals(timestamp));
  }

  static Predicate<AccumulatedPoints> isBefore(
      Function<AccumulatedPoints, Instant> field, Instant timestamp) {
    return points ->
        (field.apply(points).isBefore(timestamp) || field.apply(points).equals(timestamp));
  }

  static Comparator<AccumulatedPoints> byTransactionTimestamp() {
    return Comparator.comparing(p -> p.transactionTimestamp);
  }

  CustomerEarnedPointsEvent toEvent() {
    return CustomerEarnedPointsEvent.builder()
        .transactionId(transactionId)
        .transactionTimestamp(transactionTimestamp)
        .customerId(customerId)
        .points(points)
        .tierValidityDate(tierValidityDate)
        .redemptionValidityDate(redemptionValidityDate)
        .description(description)
        .build();
  }
}
