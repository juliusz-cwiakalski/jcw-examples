package pl.jcw.example.grpc;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.jcw.example.grpc.PerformanceTestingSinkServiceGrpc.PerformanceTestingSinkServiceStub;
import pl.jcw.example.grpc.PerformanceTestingTriggerServiceGrpc.PerformanceTestingTriggerServiceImplBase;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class PerformanceTestingTriggerService extends PerformanceTestingTriggerServiceImplBase {
  private final PerformanceTestingSinkServiceStub performanceTestingSinkServiceStub;

  @Override
  public void triggerPerformanceTest(
      ThroughPutTestTrigger request, StreamObserver<ThroughputTestResult> responseObserver) {
    StreamObserver<ThroughputTestResult> testResultObserver =
        new StreamObserver<>() {
          private ThroughputTestResult result;

          @Override
          public void onNext(ThroughputTestResult value) {
            this.result = value;
          }

          @Override
          public void onError(Throwable t) {
            log.error("Error occurred", t);
          }

          @Override
          public void onCompleted() {
            responseObserver.onNext(result);
            responseObserver.onCompleted();
          }
        };
    StreamObserver<SymbolData> outputStream =
        performanceTestingSinkServiceStub.publishMeasureThroughput(testResultObserver);
    for (int i = 0; i < request.getNumberOfMessages(); i++) {
      outputStream.onNext(
          SymbolData.newBuilder()
              .setSymbolId("Symbol:" + i)
              .setQuotation(Quotation.newBuilder().setAsk(i).setBid(i).build())
              .build());
    }
    outputStream.onCompleted();
  }
}
