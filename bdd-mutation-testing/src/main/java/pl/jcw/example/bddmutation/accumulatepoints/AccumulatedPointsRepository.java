package pl.jcw.example.bddmutation.accumulatepoints;

import static pl.jcw.example.bddmutation.accumulatepoints.AccumulatedPoints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.jcw.example.bddmutation.common.InMemoryRepository;

interface AccumulatedPointsRepository {
  <T extends AccumulatedPoints> T save(T points);

  Stream<AccumulatedPoints> findAllByCustomerIdAndEarnedAtBetweenOrderByTransactionTimestampAsc(
      String customerId, Instant earnedFrom, Instant earnedTo);

  TierPointsBalance findAllByCustomerIdAndTierValidityDateAfter(String customerId, Instant now);
}

record TierPointsBalance(BigDecimal tierPointsBalance, Instant tierValidityDate) {}

class InMemoryAccumulatedPointsRepository extends InMemoryRepository<AccumulatedPoints, String>
    implements AccumulatedPointsRepository {

  @Override
  protected String getId(AccumulatedPoints entity) {
    return entity.getTransactionId();
  }

  @Override
  public Stream<AccumulatedPoints>
      findAllByCustomerIdAndEarnedAtBetweenOrderByTransactionTimestampAsc(
          String customerId, Instant earnedFrom, Instant earnedTo) {
    return findByPredicate(
            customerIdIs(customerId).and(AccumulatedPoints.earnedBetween(earnedFrom, earnedTo)))
        .sorted(byTransactionTimestamp());
  }

  @Override
  public TierPointsBalance findAllByCustomerIdAndTierValidityDateAfter(
      String customerId, Instant now) {
    List<AccumulatedPoints> validPoints =
        findByPredicate(customerIdIs(customerId).and(isTierValid(now))).toList();
    if (validPoints.isEmpty()) {
      return new TierPointsBalance(BigDecimal.ZERO, now);
    }
    BigDecimal totalValidPoints =
        validPoints.stream()
            .map(AccumulatedPoints::getPoints)
            .reduce(BigDecimal::add)
            .orElseThrow();
    Instant validUntil =
        validPoints.stream()
            .map(AccumulatedPoints::getTierValidityDate)
            .min(Comparator.naturalOrder())
            .orElseThrow();
    return new TierPointsBalance(totalValidPoints, validUntil);
  }
}

interface JpaAccumulatedPointsRepository
    extends AccumulatedPointsRepository, JpaRepository<AccumulatedPoints, String> {}
