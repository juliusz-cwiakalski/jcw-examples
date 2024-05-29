package pl.jcw.example.bddmutation.accumulatepoints

import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned

import java.time.Instant

class CustomerPointsBalanceUpdatedEventExtensions {
  static void assertBalanceHasValuesFrom(CustomerPointsBalanceUpdatedEvent event, PointsEarned earning, Instant now) {
    assert event.balanceTimestamp() == now
    assert event.tierValidityDate() == (now + earning.tierValidity())
    assert event.tierPointsBalance() == earning.points()
    assert event.customerId() == earning.customerId()
  }
}

