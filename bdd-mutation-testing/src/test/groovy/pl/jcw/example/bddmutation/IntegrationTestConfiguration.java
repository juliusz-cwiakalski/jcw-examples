package pl.jcw.example.bddmutation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent;

@Configuration
@ComponentScan(basePackageClasses = BddAndMutationTestingApplication.class)
public class IntegrationTestConfiguration {

  @Bean
  TestEventConsumer<CustomerEarnedPointsEvent> testCustomerEarnedPointsEventConsumer() {
    return new TestEventConsumer<>(CustomerEarnedPointsEvent.class);
  }

  @Bean
  TestEventConsumer<CustomerPointsBalanceUpdatedEvent>
      testCustomerPointsBalanceUpdatedEventConsumer() {
    return new TestEventConsumer<>(CustomerPointsBalanceUpdatedEvent.class);
  }
}
