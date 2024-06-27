package pl.jcw.example.bddmutation.accumulatepoints

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Autowired
import pl.jcw.example.bddmutation.AbstractIntegrationSpec
import pl.jcw.example.bddmutation.TestEventConsumer
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned
import pl.jcw.example.bddmutation.common.IdGenerator

import java.time.Duration
import java.time.temporal.ChronoUnit

import static pl.jcw.example.bddmutation.accumulatepoints.CustomerEarnedPointsEventExtensions.assertAreEqual
import static pl.jcw.example.bddmutation.accumulatepoints.CustomerEarnedPointsEventExtensions.assertTransactionHasValuesFrom


class AccumulatePointsIntegrationSpec extends AbstractIntegrationSpec {

  String customerId = IdGenerator.nextId()

  @Autowired
  AccumulatePointsFacade accumulatePointsFacade

  @Autowired
  TestEventConsumer<CustomerPointsBalanceUpdatedEvent> balanceUpdatedEventTestEventConsumer

  @Autowired
  TestEventConsumer<CustomerEarnedPointsEvent> customerEarnedPointsEventTestEventConsumer

  def "Should publish CustomerPointsBalanceUpdated and CustomerEarnedPoints events when points are earned"(){
    given: "customer has no points earned so far and new earning is about to be done"
    PointsEarned pointsEarned = pointsEarned().build()

    when: "a customer earns points"
    //NOTE: we use facade cause there's no api available right now to earn points,
    //      in final solution we could use the same API as it would be used in prod application (via making a booking)
    accumulatePointsFacade.earn(pointsEarned)

    then: "CustomerEarnedPoints event is published"
    CustomerEarnedPointsEvent earnedPointsEvent = customerEarnedPointsEventTestEventConsumer.findEvent { it.customerId() == customerId }
    assertTransactionHasValuesFrom(earnedPointsEvent, pointsEarned)

    then: "CustomerPointsBalanceUpdated event is published"
    CustomerPointsBalanceUpdatedEvent balanceUpdatedEvent = balanceUpdatedEventTestEventConsumer.findEvent { it.customerId() == customerId }
    balanceUpdatedEvent.tierValidityDate().truncatedTo(ChronoUnit.MILLIS) == (earnedPointsEvent.transactionTimestamp() + pointsEarned.tierValidity()).truncatedTo(ChronoUnit.MILLIS)
    balanceUpdatedEvent.tierPointsBalance() == pointsEarned.points()
    balanceUpdatedEvent.customerId() == pointsEarned.customerId()
  }

  def "Should provide points earning transaction history"() {
    given: "customer earned points"
    CustomerEarnedPointsEvent firstEarnedPoints = accumulatePointsFacade.earn(pointsEarned().build())

    and: "customer earned points again"
    CustomerEarnedPointsEvent secondEarnedPoints = accumulatePointsFacade.earn(pointsEarned().build())

    and: "RestAssured request specification"
    RequestSpecification request = RestAssured.given()
    request.header("Content-Type", "application/json")

    when: "we ask for points earning history"
    Response response = request
        .param("earnedFrom", firstEarnedPoints.transactionTimestamp().toEpochMilli())
        .param("earnedTo", secondEarnedPoints.transactionTimestamp().plusMillis(1).toEpochMilli())
        .get("/customers/${customerId}/transactions")

    then: "it contains both transactions"
    response.statusCode == 200
    List<CustomerEarnedPointsEvent> transactions = response.jsonPath().getList(".", CustomerEarnedPointsEvent)
    transactions.size() == 2
    assertAreEqual(transactions[0], firstEarnedPoints)
    assertAreEqual(transactions[1], secondEarnedPoints)
  }

  def "[TODO] Should accurately record points when a booking is made"() {
    //TODO this is an integration test scenario when we have `evaluate-booking-points` and `retrieve-customer-tier`
    given: "a customer makes a booking"
    when: "points are calculated and recorded based on the booking and `receive-customer-tier`"
    then: "the points are added to the customer's account with correct validity dates"
  }

  def "[TODO] Should handle point accrual from various types of bookings"() {
    //TODO this is an integration test scenario when we have `evaluate-booking-points` and `retrieve-customer-tier`
    // could also be unit test in `evaluate-booking-points` - when we just mock modules it depends on
    given: "bookings from different categories (e.g., business, leisure)"
    when: "points are accrued from these bookings"
    then: "points are correctly calculated and recorded based on booking type and customer tier"
  }

  PointsEarned.PointsEarnedBuilder pointsEarned() {
    return PointsEarned.builder()
        .customerId(customerId)
        .points(BigDecimal.valueOf(nextLong()))
        .tierValidity(Duration.ofDays(nextLong()))
        .redemptionValidity(Duration.ofDays(nextLong()))
        .description("Points earned for " + IdGenerator.nextId())
  }

  private static long nextLong(long bound = 500) {
    new Random().nextLong(bound)
  }
}
