package pl.jcw.example.grpc;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import pl.jcw.example.grpc.PerformanceTestingSinkServiceGrpc.PerformanceTestingSinkServiceImplBase;

@Singleton
@Slf4j
public class PerformanceTestingSinkService extends PerformanceTestingSinkServiceImplBase {

  @Override
  public StreamObserver<SymbolData> publishMeasureThroughput(
      StreamObserver<ThroughputTestResult> responseObserver) {
    return new StreamObserver<>() {
      final AtomicInteger counter = new AtomicInteger();
      final AtomicLong totalMessagesSize = new AtomicLong();
      final Instant start = Instant.now();

      @Override
      public void onNext(SymbolData data) {
        long currentCounterValue = counter.incrementAndGet();
        long currentTotalMessagesSize = totalMessagesSize.addAndGet(data.getSerializedSize());
        if (currentCounterValue % 10000 == 0) {
          Duration processingDuration = getDuration();
          double throughput = getThroughput(currentCounterValue, processingDuration);
          log.info(
              "Processed {} messages in {}, throughput: {}, total messages size: {}",
              currentCounterValue,
              processingDuration,
              throughput,
              currentTotalMessagesSize);
        }
      }

      @Override
      public void onError(Throwable t) {
        log.error("Error occurred", t);
      }

      @Override
      public void onCompleted() {
        ThroughputTestResult response =
            ThroughputTestResult.newBuilder()
                .setMessagesCount(counter.get())
                .setMsgPerSecond(getThroughput())
                .setTotalTimeMillis(getDuration().toMillis())
                .build();
        log.info("Sending performance test response: {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }

      private double getThroughput() {
        return getThroughput(counter.get(), getDuration());
      }

      private double getThroughput(long currentCounterValue, Duration processingDuration) {
        return currentCounterValue * 1000.0 / processingDuration.toMillis();
      }

      private Duration getDuration() {
        return Duration.between(start, Instant.now());
      }
    };
  }
}
