package pl.jcw.example.bddmutation.accumulatepoints

import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned

import java.time.Instant

class CustomerPointsBalanceUpdatedEventExtensions {
  static boolean balanceHasValuesFrom(CustomerPointsBalanceUpdatedEvent event, PointsEarned earning, Instant now) {
    assert event.tierPointsBalance() == earning.points()
    assert event.balanceTimestamp() == now
    assert event.tierValidityDate() == (now + earning.tierValidity())
    assert event.toString() == earning.customerId()
    return true
  }
}

