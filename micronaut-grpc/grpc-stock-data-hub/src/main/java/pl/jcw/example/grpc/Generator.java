package pl.jcw.example.grpc;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class Generator {

  public static final String SYMBOL_ID = "google";
  private final ObservableStockDataService stockDataService;

  private final Random random = new Random();

  @Scheduled(fixedRate = "5s")
  void generateData() {
    SymbolData data =
        SymbolData.newBuilder()
            .setTimestamp(System.currentTimeMillis())
            .setSymbolId(SYMBOL_ID)
            .setQuotation(
                Quotation.newBuilder().setAsk(random.nextDouble()).setBid(random.nextDouble()))
            .build();
    stockDataService.publish(data);
  }
}
