package pl.jcw.example.bddmutation.accumulatepoints;

import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent;

@FunctionalInterface
interface CustomerPointsBalanceUpdatedEventPublisher {
  void publishCustomerPointsBalanceUpdatedEvent(CustomerPointsBalanceUpdatedEvent event);
}
