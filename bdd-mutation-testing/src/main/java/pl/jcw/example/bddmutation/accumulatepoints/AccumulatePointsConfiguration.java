package pl.jcw.example.bddmutation.accumulatepoints;

import java.time.Clock;

class AccumulatePointsConfiguration {
  AccumulatePointsFacade inMemoryAccumulatePointsFacade(
      Clock clock,
      CustomerEarnedPointsEventPublisher customerEarnedPointsEventPublisher,
      CustomerPointsBalanceUpdatedEventPublisher customerPointsBalanceUpdatedEventPublisher) {
    return new AccumulatePointsFacade(
        clock, customerEarnedPointsEventPublisher, customerPointsBalanceUpdatedEventPublisher);
  }
}
