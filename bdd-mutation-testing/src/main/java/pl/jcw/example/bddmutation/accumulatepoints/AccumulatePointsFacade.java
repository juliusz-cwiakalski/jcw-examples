package pl.jcw.example.bddmutation.accumulatepoints;

import java.time.Clock;
import java.time.Instant;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent;
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccumulatePointsFacade {

  Clock clock;
  CustomerEarnedPointsEventPublisher customerEarnedPointsEventPublisher;
  CustomerPointsBalanceUpdatedEventPublisher customerPointsBalanceUpdatedEventPublisher;
  AccumulatedPointsRepository accumulatedPointsRepository;

  public CustomerEarnedPointsEvent earn(PointsEarned pointsEarned) {
    AccumulatedPoints earning = new AccumulatedPoints(pointsEarned, clock.instant());
    AccumulatedPoints saved = accumulatedPointsRepository.save(earning);
    CustomerEarnedPointsEvent event = saved.toEvent();
    customerEarnedPointsEventPublisher.publishCustomerEarnedPointsEvent(event);
    CustomerPointsBalanceUpdatedEvent currentPointsBalance =
        getCurrentPointsBalance(pointsEarned.customerId());
    customerPointsBalanceUpdatedEventPublisher.publishCustomerPointsBalanceUpdatedEvent(
        currentPointsBalance);
    return event;
  }

  public CustomerPointsBalanceUpdatedEvent getCurrentPointsBalance(String customerId) {
    Instant now = clock.instant();
    TierPointsBalance balance =
        accumulatedPointsRepository.findAllByCustomerIdAndTierValidityDateAfter(customerId, now);
    return CustomerPointsBalanceUpdatedEvent.builder()
        .customerId(customerId)
        .tierPointsBalance(balance.tierPointsBalance())
        .balanceTimestamp(now)
        .tierValidityDate(balance.tierValidityDate())
        .build();
  }

  public Stream<CustomerEarnedPointsEvent> findEarningTransactions(
      String customerId, Instant earnedFrom, Instant earnedTo) {
    return accumulatedPointsRepository
        .findAllByCustomerIdAndEarnedAtBetweenOrderByTransactionTimestampAsc(
            customerId, earnedFrom, earnedTo)
        .map(AccumulatedPoints::toEvent);
  }
}
