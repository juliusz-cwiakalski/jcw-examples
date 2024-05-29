package pl.jcw.example.bddmutation.accumulatepoints

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import pl.jcw.example.bddmutation.IntegrationTestConfiguration
import pl.jcw.example.bddmutation.accumulatepoints.api.PointsEarned
import pl.jcw.example.bddmutation.common.IdGenerator
import pl.jcw.example.bddmutation.common.InMemoryRepository
import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

abstract class AbstractAccumulatedPointsRepositorySpec extends Specification {

  Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS)
  String customerId = randomString()
  String description = randomString()
  Duration tierValidity = Duration.ofDays(randomLong(500))
  Duration redemptionValidity = Duration.ofDays(randomLong(500))
  BigDecimal points = BigDecimal.valueOf(randomLong())

  def "Should save AccumulatedPoints"() {
    given: "AccumulatedPoints instance"
    AccumulatedPoints newPoints = createAccumulatedPointsPersistedAt()

    when: "save is called"
    AccumulatedPoints persistedPoints = getRepository().save(newPoints)
    Optional<AccumulatedPoints> optionalLoadedPoints = getRepository().findById(newPoints.getTransactionId())

    then: "AccumulatedPoints is saved"
    optionalLoadedPoints.isPresent()
    assertAccumulatedPointsEqual(newPoints, persistedPoints)
    assertAccumulatedPointsEqual(newPoints, optionalLoadedPoints.get())
  }

  @Transactional
  def "Should find all by customer id and transaction timestamp between certain dates"() {
    given: "There are some points accumulated in search time range and before and after"
    Instant from = now.minusSeconds(60)
    Instant middle = now.minusSeconds(30)
    Instant to = now

    AccumulatedPoints pointsAtBeginningOfRange = createAccumulatedPointsPersistedAt(from)
    AccumulatedPoints pointsInTheMiddleOfRange = createAccumulatedPointsPersistedAt(middle)
    AccumulatedPoints pointsAtTheEndOfRange = createAccumulatedPointsPersistedAt(to)

    AccumulatedPoints pointsBeforeTheRange = createAccumulatedPointsPersistedAt(from.minusSeconds(60))
    AccumulatedPoints pointsAfterTheRange = createAccumulatedPointsPersistedAt(to.plusSeconds(60))

    when: "findAllByCustomerIdAndTransactionTimestampBetweenOrderByTransactionTimestampAsc($customerId, $from, $to) is called with"
    List<AccumulatedPoints> pointsInRange = getRepository().findAllByCustomerIdAndTransactionTimestampBetweenOrderByTransactionTimestampAsc(customerId, from, to).toList()


    then: "Correct AccumulatedPoints are returned"
    pointsInRange.size() == 3
    assertAccumulatedPointsEqual(pointsInRange[0], pointsAtBeginningOfRange)
    assertAccumulatedPointsEqual(pointsInRange[1], pointsInTheMiddleOfRange)
    assertAccumulatedPointsEqual(pointsInRange[2], pointsAtTheEndOfRange)
  }

  def "Should calculated TierPointsBalance for a given timestamp"() {
    given: "customer earned points 3 times with 60 seconds intervals"
    AccumulatedPoints points1 = createAccumulatedPointsPersistedAt(now)
    AccumulatedPoints points2 = createAccumulatedPointsPersistedAt(now.plusSeconds(60))
    AccumulatedPoints points3 = createAccumulatedPointsPersistedAt(now.plusSeconds(120))

    when: "we calculate balance 30 seconds after first earned points tierValidityDate expires"
    TierPointsBalance result = getRepository().calculateCustomerTierPointsBalanceForDate(
        customerId,
        points1.toEvent().tierValidityDate().plusSeconds(30))

    then: "TierPointsBalance considering only the second and third points earning is returned"
    result.tierValidityDate() == points2.tierValidityDate
    result.tierPointsBalance() == points2.toEvent().points() + points3.toEvent().points()
  }

  private AccumulatedPoints createAccumulatedPointsPersistedAt(Instant now = now, Closure modifyBuilder = { it }) {
    PointsEarned.PointsEarnedBuilder builder = PointsEarned.builder()
        .customerId(customerId)
        .points(points)
        .description(description)
        .tierValidity(tierValidity)
        .redemptionValidity(redemptionValidity)
    modifyBuilder(builder)
    return getRepository().save(new AccumulatedPoints(builder.build(), now))
  }

  private static String randomString() {
    IdGenerator.nextId()
  }

  private static long randomLong(long bound = 10000) {
    new Random().nextLong(bound)
  }

  protected abstract AccumulatedPointsRepository getRepository();

  void assertAccumulatedPointsEqual(AccumulatedPoints expected, AccumulatedPoints actual) {
    assert expected.transactionId == actual.transactionId
    assert expected.transactionTimestamp == actual.transactionTimestamp
    assert expected.customerId == actual.customerId
    assert expected.points == actual.points
    assert expected.tierValidityDate == actual.tierValidityDate
    assert expected.redemptionValidityDate == actual.redemptionValidityDate
    assert expected.description == actual.description
  }
}

class InMemoryAccumulatedPointsRepositorySpec extends AbstractAccumulatedPointsRepositorySpec {

  InMemoryRepository accumulatedPointsRepository = new InMemoryAccumulatedPointsRepository()

  @Override
  protected AccumulatedPointsRepository getRepository() {
    return accumulatedPointsRepository
  }
}

@ContextConfiguration(classes = IntegrationTestConfiguration)
class JpaAccumulatedPointsRepositoryIntegrationSpec extends AbstractAccumulatedPointsRepositorySpec {

  @Autowired
  JpaAccumulatedPointsRepository accumulatedPointsRepository

  @Override
  protected AccumulatedPointsRepository getRepository() {
    return accumulatedPointsRepository
  }
}
