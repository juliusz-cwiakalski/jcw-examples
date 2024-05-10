package pl.jcw.example.bddmutation.accumulatepoints

import spock.lang.Specification

class AccumulatePointsSpec extends Specification {

  def "Should record points and provide history of earned points"(){
    given: "points are earned"
    when: "we ask for points earning history"
    then: "history contains the earned points and details (when was earned, etc)"
  }

  def "Should publish new transaction with earned points"(){
    when: "points are earned"
    then: "CustomerEarnedPoints event is emitted with points and their validity dates"
  }

  def "Should update tier points balance after each transaction"() {
    given: "a customer earns points"
    when: "the points are added to the customer's account"
    then: "a CustomerPointsBalanceUpdated event is published with the new balance"
  }

  def "Should consider points expiry when publishing points balance after each transaction"() {
    given: "a customer earns points and the points are added to the customer's account"
    and: "time of points validity passes"
    when: "a customer earns points again"
    then: "a CustomerPointsBalanceUpdated event is published and it includes only not expired points"
  }

  def "Should calculate balance expiry date considering when first non expired points will expire"(){
    given: "customer earns 10 points valid till 2010-02-01"
    and: "it's 2010-01-01"
    when: "customer earns 20 points valid till 2010-03-01"
    then: "CustomerPointsBalanceUpdated is emitted with validity date 2010-02-01"
    when: "time passes to 2010-02-02 and customer earns points again that are valid till 2010-04-01"
    then: "CustomerPointsBalanceUpdated is emitted with validity date 2010-03-01"
  }

  def "Should provide a comprehensive history of points earned"() {
    given: "a customer has multiple points transactions"
    when: "a request for points history is made for certain time frame"
    then: "the history includes all points earned with their respective earning dates and validity"
  }

  def "Should handle points redemption by deducting from the balance"() {
    //TODO this is a test for `redeem-points` module - move there once it's created
    given: "a customer redeems some points"
    when: "points are deducted from the balance"
    then: "the points balance is updated and a CustomerPointsBalanceUpdated event is published"
  }

  def "Should expire points correctly based on their spending validity date"() {
    //TODO this is a test for `redeem-points` module - move there once it's created
    given: "points have reached their spending validity date"
    when: "the system checks for point validity"
    then: "expired points are removed from the balance and the change is reflected in the points history"
  }

  def "Should maintain accurate tier status based on points balance"() {
    //TODO this is a test for `retrieve-customer-tier` - move there once it's created
    given: "a customer's points balance changes"
    and: "this change affects their tier status"
    when: "the tier status is evaluated"
    then: "the customer's tier is updated based on the current points balance"
  }
}
