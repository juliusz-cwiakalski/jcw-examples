package pl.jcw.example.bddmutation.accumulatepoints;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccumulatePointsConfiguration {
  AccumulatePointsFacade inMemoryAccumulatePointsFacade(
      Clock clock,
      CustomerEarnedPointsEventPublisher customerEarnedPointsEventPublisher,
      CustomerPointsBalanceUpdatedEventPublisher customerPointsBalanceUpdatedEventPublisher) {
    InMemoryAccumulatedPointsRepository repository = new InMemoryAccumulatedPointsRepository();
    return accumulatePointsFacade(
        clock,
        customerEarnedPointsEventPublisher,
        customerPointsBalanceUpdatedEventPublisher,
        repository);
  }

  @Bean
  AccumulatePointsFacade accumulatePointsFacade(
      Clock clock,
      CustomerEarnedPointsEventPublisher customerEarnedPointsEventPublisher,
      CustomerPointsBalanceUpdatedEventPublisher customerPointsBalanceUpdatedEventPublisher,
      InMemoryAccumulatedPointsRepository repository) {
    return new AccumulatePointsFacade(
        clock,
        customerEarnedPointsEventPublisher,
        customerPointsBalanceUpdatedEventPublisher,
        repository);
  }
}
