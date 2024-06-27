package pl.jcw.example.bddmutation.accumulatepoints.web;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.jcw.example.bddmutation.accumulatepoints.AccumulatePointsFacade;
import pl.jcw.example.bddmutation.accumulatepoints.api.CustomerEarnedPointsEvent;

@RestController
public class AccumulatePointsController {

  private final AccumulatePointsFacade accumulatePointsFacade;

  public AccumulatePointsController(AccumulatePointsFacade accumulatePointsFacade) {
    this.accumulatePointsFacade = accumulatePointsFacade;
  }

  @GetMapping("/customers/{customerId}/transactions")
  @Transactional
  public ResponseEntity<Stream<CustomerEarnedPointsEvent>> findEarningTransactions(
      @PathVariable String customerId,
      @RequestParam Instant earnedFrom,
      @RequestParam Instant earnedTo) {
    Stream<CustomerEarnedPointsEvent> transactions =
        accumulatePointsFacade.findEarningTransactions(customerId, earnedFrom, earnedTo);
    return ResponseEntity.ok(transactions);
  }
}
