package pl.jcw.example.bddmutation.accumulatepoints;

import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent;

@FunctionalInterface
interface CustomerEarnedPointsEventPublisher {
  void publishCustomerEarnedPointsEvent(CustomerEarnedPointsEvent event);
}
