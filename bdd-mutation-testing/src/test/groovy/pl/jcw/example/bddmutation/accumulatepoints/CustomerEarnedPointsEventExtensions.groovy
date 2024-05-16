package pl.jcw.example.bddmutation.accumulatepoints

import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned

class CustomerEarnedPointsEventExtensions {
  static boolean transactionHasValuesFrom(CustomerEarnedPointsEvent event, PointsEarned pointsEarning){
    assert event.transactionTimestamp() != null
    assert event.transactionId() != null
    assert event.customerId() == pointsEarning.customerId()
    assert event.points() == pointsEarning.points()
    assert event.description() == pointsEarning.description()
    assert event.tierValidityDate() == event.transactionTimestamp() + pointsEarning.tierValidity()
    assert event.redemptionValidityDate() == event.transactionTimestamp() + pointsEarning.redemptionValidity()
    return true
  }
}
