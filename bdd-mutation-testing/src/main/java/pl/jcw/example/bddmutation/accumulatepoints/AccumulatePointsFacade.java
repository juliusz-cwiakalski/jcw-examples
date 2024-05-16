package pl.jcw.example.bddmutation.accumulatepoints;

import java.time.Clock;
import java.time.Instant;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent;
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccumulatePointsFacade {

  Clock clock;
  CustomerEarnedPointsEventPublisher customerEarnedPointsEventPublisher;
  CustomerPointsBalanceUpdatedEventPublisher customerPointsBalanceUpdatedEventPublisher;

  public CustomerEarnedPointsEvent earn(PointsEarned pointsEarned) {
    return CustomerEarnedPointsEvent.builder().build();
  }

  public Stream<CustomerEarnedPointsEvent> findEarningTransactions(
      String customerId, Instant earnedFrom, Instant earnedTo) {
    return Stream.of(CustomerEarnedPointsEvent.builder().build());
  }
}
