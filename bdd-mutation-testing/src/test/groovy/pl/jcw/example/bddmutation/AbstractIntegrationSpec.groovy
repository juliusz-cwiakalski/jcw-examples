package pl.jcw.example.bddmutation

import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = IntegrationTestConfiguration)
class AbstractIntegrationSpec extends Specification {

  @LocalServerPort
  int localServerPort

  def setup() {
    RestAssured.baseURI = "http://localhost"
    RestAssured.port = localServerPort
  }
}
