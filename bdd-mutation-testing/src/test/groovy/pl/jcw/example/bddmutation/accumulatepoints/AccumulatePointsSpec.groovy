package pl.jcw.example.bddmutation.accumulatepoints


import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerPointsBalanceUpdatedEvent
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent
import pl.jcw.example.bddmutation.common.IdGenerator
import spock.lang.Specification
import spock.util.time.MutableClock

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import static java.time.Duration.between
import static pl.jcw.example.bddmutation.accumulatepoints.CustomerEarnedPointsEventExtensions.assertTransactionHasValuesFrom
import static pl.jcw.example.bddmutation.accumulatepoints.CustomerPointsBalanceUpdatedEventExtensions.assertBalanceHasValuesFrom

class AccumulatePointsSpec extends Specification {

  String customerId = IdGenerator.nextId()
  MutableClock clock = new MutableClock()
  List<CustomerEarnedPointsEvent> earnedPointsEvents = []
  List<CustomerPointsBalanceUpdatedEvent> balanceUpdatedEvents = []
  AccumulatePointsFacade accumulatePointsFacade = new AccumulatePointsConfiguration().inMemoryAccumulatePointsFacade(clock, earnedPointsEvents.&add, balanceUpdatedEvents.&add)

  def "Should record points and provide history of earned points"() {
    given: "points are earned"
    PointsEarned pointsEarned = pointsEarned().build()
    accumulatePointsFacade.earn(pointsEarned)

    when: "we ask for points earning history"
    List<CustomerEarnedPointsEvent> transactions = accumulatePointsFacade.findEarningTransactions(
        customerId,
        now().minus(100, ChronoUnit.DAYS),
        now().plus(100, ChronoUnit.DAYS)).toList()

    then: "the history contains the earned points and details such as the earning date"
    transactions.size() == 1
    assertTransactionHasValuesFrom(transactions[0], pointsEarned)
    transactions[0].transactionTimestamp() == now()
  }

  def "Should provide a comprehensive history of points earned"() {
    given: "a customer has multiple points transactions"
    accumulatePointsFacade.earn(pointsEarned().build())
    clock.next()
    CustomerEarnedPointsEvent transaction2 = accumulatePointsFacade.earn(pointsEarned().build())
    clock.next()
    CustomerEarnedPointsEvent transaction3 = accumulatePointsFacade.earn(pointsEarned().build())

    when: "a request for points history is made for a certain time frame"
    List<CustomerEarnedPointsEvent> transactions = accumulatePointsFacade.findEarningTransactions(
        customerId,
        transaction2.transactionTimestamp(),
        transaction3.transactionTimestamp()).toList()

    then: "the history includes all points earned within the specified dates"
    transactions.size() == 2
    transactions == [transaction2, transaction3]
  }

  def "Should publish a new transaction event when points are earned"() {
    when: "points are earned"
    PointsEarned pointsEarned = pointsEarned().build()
    accumulatePointsFacade.earn(pointsEarned)

    then: "a CustomerEarnedPoints event is emitted with points and their validity dates"
    earnedPointsEvents.size() == 1
    assertTransactionHasValuesFrom(earnedPointsEvents[0], pointsEarned)
    earnedPointsEvents[0].transactionTimestamp() == now()
  }

  def "Should update the tier points balance after each transaction"() {
    given: "a customer earns points"
    PointsEarned pointsEarned = pointsEarned().build()

    when: "the points are added to the customer's account"
    accumulatePointsFacade.earn(pointsEarned)

    then: "a CustomerPointsBalanceUpdated event is published with the new balance"
    balanceUpdatedEvents.size() == 1
    assertBalanceHasValuesFrom(balanceUpdatedEvents[0], pointsEarned, now())
  }

  def "Should provide the current points balance on demand"() {
    given: "a customer earns points that are added to their account"
    PointsEarned pointsEarned = pointsEarned().build()
    accumulatePointsFacade.earn(pointsEarned)

    when: "we ask for the current points balance"
    CustomerPointsBalanceUpdatedEvent currentPointsBalance = accumulatePointsFacade.getCurrentPointsBalance(customerId)

    then: "a CustomerPointsBalanceUpdated event contains up-to-date details"
    assertBalanceHasValuesFrom(currentPointsBalance, pointsEarned, now())
  }

  def "Should provide zero points balance if we ask it for customer that has no earnings"() {
    given: "a customer did not earn any points yet"

    when: "we ask for the current points balance"
    CustomerPointsBalanceUpdatedEvent currentPointsBalance = accumulatePointsFacade.getCurrentPointsBalance(customerId)

    then: "a CustomerPointsBalanceUpdated with zero balance is returned"
    currentPointsBalance.customerId() == customerId
    currentPointsBalance.tierPointsBalance() == BigDecimal.ZERO
    currentPointsBalance.tierValidityDate() == now()
    currentPointsBalance.balanceTimestamp() == now()
  }

  def "Should consider points expiry when publishing the points balance after each transaction"() {
    given: "a customer earns points and the points are added to the customer's account"
    PointsEarned pointsThatExpire = pointsEarned().build()
    Instant initialEarningTime = now()
    accumulatePointsFacade.earn(pointsThatExpire)

    and: "the points' validity period passes"
    clock.plus(pointsThatExpire.tierValidity().plusSeconds(1))

    when: "a customer earns points again"
    PointsEarned nextPointsEarned = pointsEarned().build()
    accumulatePointsFacade.earn(nextPointsEarned)

    then: "a CustomerPointsBalanceUpdated event is published and includes only non-expired points"
    balanceUpdatedEvents.size() == 2
    assertBalanceHasValuesFrom(balanceUpdatedEvents[0], pointsThatExpire, initialEarningTime)
    assertBalanceHasValuesFrom(balanceUpdatedEvents[1], nextPointsEarned, now())
  }

  def "Should calculate balance expiry date considering when first non-expired points will expire and not count expired points into balance"() {
    given: "it's 2024-01-01"
    clock.setInstant(toDate("2024-01-01"))

    and: "a customer earns points the first time that are valid until 2024-02-01"
    PointsEarned firstEarning = pointsEarned().tierValidity(between(now(), toDate("2024-02-01"))).build()
    accumulatePointsFacade.earn(firstEarning)

    when: "a customer earns points a second time that are valid until 2024-03-01"
    PointsEarned secondEarning = pointsEarned().tierValidity(between(now(), toDate("2024-03-01"))).build()
    accumulatePointsFacade.earn(secondEarning)

    then: "CustomerPointsBalanceUpdated is emitted with validity date 2024-02-01"
    balanceUpdatedEvents.size() == 2
    balanceUpdatedEvents[0].tierValidityDate() == toDate("2024-02-01")
    balanceUpdatedEvents[0].tierPointsBalance() == firstEarning.points()
    balanceUpdatedEvents[1].tierValidityDate() == toDate("2024-02-01")
    balanceUpdatedEvents[1].tierPointsBalance() == firstEarning.points() + secondEarning.points()

    when: "time passes to 2024-02-02"
    clock.setInstant(toDate("2024-02-02"))

    and: "the customer earns points a third time that are valid until 2024-04-01"
    PointsEarned thirdEarning = pointsEarned().tierValidity(between(now(), toDate("2024-04-01"))).build()
    accumulatePointsFacade.earn(thirdEarning)

    then: "CustomerPointsBalanceUpdated is emitted with validity date 2024-03-01"
    balanceUpdatedEvents.size() == 3
    balanceUpdatedEvents[2].tierValidityDate() == toDate("2024-03-01")
    balanceUpdatedEvents[2].tierPointsBalance() == secondEarning.points() + thirdEarning.points()
  }

  def "[TODO] Should handle points redemption by deducting from the balance"() {
    // TODO: this is a test for the `redeem-points` module - move there once it's created
    given: "a customer redeems some points"
    when: "points are deducted from the balance"
    then: "the points balance is updated and a CustomerPointsBalanceUpdated event is published"
  }

  def "[TODO] Should expire points correctly based on their spending validity date"() {
    // TODO: this is a test for the `redeem-points` module - move there once it's created
    given: "points have reached their spending validity date"
    when: "the system checks for point validity"
    then: "expired points are removed from the balance and the change is reflected in the points history"
  }

  def "[TODO] Should maintain accurate tier status based on points balance"() {
    // TODO: this is a test for the `retrieve-customer-tier` module - move there once it's created
    given: "a customer's points balance changes"
    and: "this change affects their tier status"
    when: "the tier status is evaluated"
    then: "the customer's tier is updated based on the current points balance"
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

  Instant toDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    LocalDate localDate = LocalDate.parse(dateString, formatter)
    ZoneId defaultZoneId = ZoneId.systemDefault()
    ZonedDateTime zonedDateTime = localDate.atStartOfDay(defaultZoneId)
    return zonedDateTime.toInstant()
  }

  Instant now() {
    return clock.instant()
  }
}
