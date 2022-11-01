package pl.jcw.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import pl.jcw.example.grpc.StockDataServiceGrpc.StockDataServiceStub;

@Singleton
@Slf4j
public class QuotationsProvider {
  private final StockDataServiceStub stockDataService;

  private final PriorityQueue<QueuedData> dataQueue;
  private final Random random = new Random();
  private StreamObserver<SymbolData> dataStream = null;

  public QuotationsProvider(StockDataServiceStub stockDataService) {
    this.stockDataService = stockDataService;
    dataQueue = new PriorityQueue<>(Comparator.comparing(QueuedData::nextTimestamp));
    initQuotationData();
  }

  private QueuedData generateQuotationData(String symbolId) {
    long timestamp = System.currentTimeMillis();
    return new QueuedData(
        timestamp,
        SymbolData.newBuilder()
            .setTimestamp(timestamp)
            .setSymbolId(symbolId)
            .setQuotation(nextQuotation(random.nextDouble(500) + 10))
            .build());
  }

  private void initQuotationData() {
    Stream.of("google", "microsoft", "amazon", "netflix")
        .map(this::generateQuotationData)
        .forEach(dataQueue::add);
  }

  @Scheduled(fixedRate = "250ms")
  void generateQuotations() {
    while (dataQueue.peek() != null
        && dataQueue.peek().nextTimestamp() < System.currentTimeMillis()) {
      QueuedData nextData = nextData(dataQueue.poll());
      dataQueue.add(nextData);
      log.info("Generated: {}", nextData);
      sendToDataHub(nextData.data);
    }
  }

  private void sendToDataHub(SymbolData data) {
    assureConnected();
    dataStream.onNext(data);
  }

  private void assureConnected() {
    if (dataStream == null) {
      this.dataStream =
          this.stockDataService.publish(
              new StreamObserver<>() {
                @Override
                public void onNext(Empty value) {
                  // ignore response
                }

                @Override
                public void onError(Throwable t) {
                  log.error("Error occurred while data streaming", t);
                  closeConnection();
                }

                @Override
                public void onCompleted() {
                  closeConnection();
                }
              });
    }
  }

  private void closeConnection() {
    try {
      if (dataStream != null) {
        dataStream.onCompleted();
      }
    } finally {
      dataStream = null;
    }
  }

  private QueuedData nextData(QueuedData last) {
    long nextPublishTime = last.nextTimestamp() + random.nextLong(10000) + 2000;
    SymbolData lastData = last.data();
    SymbolData nextData =
        lastData.toBuilder()
            .setTimestamp(System.currentTimeMillis())
            .setQuotation(nextQuotation(lastData.getQuotation().getBid()))
            .build();
    return new QueuedData(nextPublishTime, nextData);
  }

  private Quotation nextQuotation(double lastBid) {
    double nextBid = round(lastBid * (0.99 + random.nextDouble(0.02)));
    double nextAsk = round(nextBid * (1 + random.nextDouble(0.01) + 0.005));
    return Quotation.newBuilder().setBid(nextBid).setAsk(nextAsk).build();
  }

  private double round(double value) {
    return Math.round(value * 100.0) / 100.0;
  }

  record QueuedData(long nextTimestamp, SymbolData data) {}
}
