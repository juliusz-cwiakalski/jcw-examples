package pl.jcw.example.bddmutation.accumulatepoints

import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned

import java.time.Instant
import java.time.temporal.ChronoUnit

class CustomerEarnedPointsEventExtensions {
  static void assertTransactionHasValuesFrom(CustomerEarnedPointsEvent event, PointsEarned pointsEarning){
    assert event.transactionTimestamp() != null
    assert event.transactionId() != null
    assert event.customerId() == pointsEarning.customerId()
    assert event.points() == pointsEarning.points()
    assert event.description() == pointsEarning.description()
    assert event.tierValidityDate() == event.transactionTimestamp() + pointsEarning.tierValidity()
    assert event.redemptionValidityDate() == event.transactionTimestamp() + pointsEarning.redemptionValidity()
  }

  static void assertAreEqual(CustomerEarnedPointsEvent expected, CustomerEarnedPointsEvent actual) {
    assert expected.transactionId() == actual.transactionId()
    assert truncated(expected.transactionTimestamp()) == truncated(actual.transactionTimestamp())
    assert expected.customerId() == actual.customerId()
    assert expected.points() == actual.points()
    assert truncated(expected.tierValidityDate()) == truncated(actual.tierValidityDate())
    assert truncated(expected.redemptionValidityDate()) == truncated(actual.redemptionValidityDate())
    assert expected.description() == actual.description()
  }

  private static Instant truncated(Instant instant) {
    return instant.truncatedTo(ChronoUnit.MILLIS)
  }
}
