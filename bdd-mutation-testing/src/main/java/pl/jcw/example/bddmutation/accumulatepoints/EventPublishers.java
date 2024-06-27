package pl.jcw.example.bddmutation.accumulatepoints;

import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent;

@FunctionalInterface
interface CustomerEarnedPointsEventPublisher {
  void publishCustomerEarnedPointsEvent(CustomerEarnedPointsEvent event);
}

@FunctionalInterface
interface CustomerPointsBalanceUpdatedEventPublisher {
  void publishCustomerPointsBalanceUpdatedEvent(CustomerPointsBalanceUpdatedEvent event);
}
