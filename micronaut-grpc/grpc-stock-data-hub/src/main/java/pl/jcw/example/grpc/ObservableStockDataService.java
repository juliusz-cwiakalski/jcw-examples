package pl.jcw.example.grpc;

import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.jcw.example.grpc.SymbolDataRequest.DataType;

@Singleton
@Slf4j
public class ObservableStockDataService {

  private final Map<String, StockDataStore> stockData = new ConcurrentHashMap<>();

  private final Map<String, Set<StockDataObserver>> observers = new ConcurrentHashMap<>();

  public void publish(SymbolData data) {
    long dataAge = System.currentTimeMillis() - data.getTimestamp();
    log.info("Publishing {} for symbol '{}' that is {}ms old",getDataType(data).orElse(null), data.getSymbolId(), dataAge);
    stockData.computeIfAbsent(data.getSymbolId(), id -> new StockDataStore()).storeData(data);
  }

  public Stream<CompanyDetails> streamAllCompaniesDetails() {
    return stockData.values().stream()
        .map(d -> d.stockData.get(DataType.DETAILS))
        .filter(Objects::nonNull)
        .map(SymbolData::getDetails);
  }

  public Stream<SymbolData> streamAllData() {
    return stockData.values().stream().flatMap(StockDataStore::streamAllData);
  }

  @RequiredArgsConstructor
  public class StockDataObserver implements AutoCloseable {
    private static final AtomicInteger idGenerator = new AtomicInteger();

    private final int id = idGenerator.getAndIncrement();

    private final Consumer<SymbolData> consumer;

    private final Map<String, Set<DataType>> subscriptions = new ConcurrentHashMap<>();

    public void subscribe(SymbolDataRequest request) {
      Set<DataType> observedDataTypes =
          subscriptions.computeIfAbsent(request.getSymbolId(), k -> ConcurrentHashMap.newKeySet());
      observedDataTypes.addAll(request.getTypesList());
      if (observedDataTypes.contains(DataType.ALL) && observedDataTypes.size() > 1) {
        observedDataTypes.retainAll(List.of(DataType.ALL));
      }
      observers
          .computeIfAbsent(request.getSymbolId(), i -> ConcurrentHashMap.newKeySet())
          .add(this);

      notifySubscribed(request);

      log.info("{} subscribed to {}, all observers: \n{}", this, request, observers);
    }

    private void notifySubscribed(SymbolDataRequest request) {
      StockDataStore stockDataStore = stockData.get(request.getSymbolId());
      if (stockDataStore != null) {
        if (request.getTypesList().contains(DataType.ALL)) {
          stockDataStore.stockData.values().forEach(this::notifyObserver);
        } else {
          stockDataStore.stockData.entrySet().stream()
              .filter(e -> request.getTypesList().contains(e.getKey()))
              .map(Entry::getValue)
              .forEach(this::notifyObserver);
        }
      }
    }

    private void notifyObserver(SymbolData data) {
      Set<DataType> dataTypes = subscriptions.get(data.getSymbolId());
      if (!CollectionUtils.isEmpty(dataTypes)
          && (dataTypes.contains(DataType.ALL) || observesData(dataTypes, data))) {
        consumer.accept(data);
      }
    }

    private boolean observesData(Set<DataType> dataTypes, SymbolData data) {
      return getDataType(data).filter(dataTypes::contains).isPresent();
    }

    public void stopSubscriptions() {
      subscriptions.keySet().forEach(this::stopObserving);
      log.info("De-registered observer {}, remaining observers: {}", this, observers);
    }

    private void stopObserving(String symbolId) {
      observers.get(symbolId).remove(this);
    }

    @Override
    public void close() {
      stopSubscriptions();
    }

    @Override
    public String toString() {
      return "obrv#" + id;
    }
  }

  private class StockDataStore {
    private final Map<DataType, SymbolData> stockData = new ConcurrentHashMap<>();

    public void storeData(SymbolData data) {
      getDataType(data).ifPresent(type -> stockData.put(type, data));
      Set<StockDataObserver> stockDataObservers = observers.get(data.getSymbolId());
      if (stockDataObservers != null) {
        stockDataObservers.forEach(observer -> observer.notifyObserver(data));
      }
    }

    public Stream<SymbolData> streamAllData() {
      return stockData.values().stream();
    }
  }

  public StockDataObserver createObserver(Consumer<SymbolData> consumer) {
    return new StockDataObserver(consumer);
  }

  private static Optional<DataType> getDataType(SymbolData data) {
    if (data.hasNews()) {
      return Optional.of(DataType.NEWS);
    }
    if (data.hasQuotation()) {
      return Optional.of(DataType.QUOTATION);
    }
    if (data.hasDetails()) return Optional.of(DataType.DETAILS);
    return Optional.empty();
  }
}
