package pl.jcw.example.grpc;

import jakarta.inject.Singleton;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import pl.jcw.example.grpc.ObservableStockDataService.StockDataObserver;
import pl.jcw.example.grpc.SymbolDataRequest.DataType;

//@Singleton
@Slf4j
public class LoggingObserver implements Consumer<SymbolData> {

  private final StockDataObserver observer;

  public LoggingObserver(ObservableStockDataService stockDataService) {
    this.observer = stockDataService.createObserver(this);
    this.watchSymbol(Generator.SYMBOL_ID);
  }

  private void watchSymbol(String symbolId) {
    observer.subscribe(
        SymbolDataRequest.newBuilder().setSymbolId(symbolId).addTypes(DataType.ALL).build());
  }

  @Override
  public void accept(SymbolData symbolData) {
    log.info("Received {}", symbolData);
  }
}
