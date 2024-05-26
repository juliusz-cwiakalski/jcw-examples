package pl.jcw.example.bddmutation.accumulatepoints

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import pl.jcw.example.bddmutation.IntegrationTestConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = IntegrationTestConfiguration)
class AccumulatePointsIntegrationSpec extends Specification {

  @Autowired
  AccumulatePointsFacade accumulatePointsFacade

  def "Should publish CustomerPointsBalanceUpdated and CustomerEarnedPoints events when points are earned"(){
    given: "customer has no points earned so far"
    when: "a customer earns points"
    then: "CustomerPointsBalanceUpdated event is published"

    then: "CustomerEarnedPoints event is published"
  }

  def "Should provide points earning transaction history"(){
    given: "customer earned points"
    and: "customer earned points again"
    when: "we ask for points earning history"
    then: "it contains both transactions"
  }

  def "Should accurately record points when a booking is made"() {
    //TODO this is an integration test scenario when we have `evaluate-booking-points` and `retrieve-customer-tier`
    given: "a customer makes a booking"
    when: "points are calculated and recorded based on the booking and `receive-customer-tier`"
    then: "the points are added to the customer's account with correct validity dates"
  }

  def "Should handle point accrual from various types of bookings"() {
    //TODO this is an integration test scenario when we have `evaluate-booking-points` and `retrieve-customer-tier`
    // could also be unit test in `evaluate-booking-points` - when we just mock modules it depends on
    given: "bookings from different categories (e.g., business, leisure)"
    when: "points are accrued from these bookings"
    then: "points are correctly calculated and recorded based on booking type and customer tier"
  }
}
