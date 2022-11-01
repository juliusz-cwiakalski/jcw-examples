package pl.jcw.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.jcw.example.grpc.ObservableStockDataService.StockDataObserver;
import pl.jcw.example.grpc.StockDataServiceGrpc.StockDataServiceImplBase;

@Singleton
@Slf4j
@RequiredArgsConstructor
class GrpcStockDataServiceImpl extends StockDataServiceImplBase {

  private final ObservableStockDataService stockDataService;

  @Override
  public StreamObserver<SymbolDataRequest> subscribe(StreamObserver<SymbolData> responseObserver) {

    return new StreamObserver<>() {
      final StockDataObserver observer = stockDataService.createObserver(responseObserver::onNext);

      @Override
      public void onNext(SymbolDataRequest request) {
        observer.subscribe(request);
      }

      @Override
      public void onError(Throwable t) {
        observer.stopSubscriptions();
        log.error(
            "Error occurred while handling subscription, de-registering observer: {}", observer, t);
      }

      @Override
      public void onCompleted() {
        observer.stopSubscriptions();
      }
    };
  }

  @Override
  public StreamObserver<SymbolData> publish(StreamObserver<Empty> responseObserver) {
    return new StreamObserver<>() {
      @Override
      public void onNext(SymbolData value) {
        stockDataService.publish(value);
      }

      @Override
      public void onError(Throwable t) {
        log.error("Error occurred while publishing data", t);
      }

      @Override
      public void onCompleted() {
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
      }
    };
  }

  @Override
  public void getAllSymbols(Empty request, StreamObserver<CompanyDetails> responseObserver) {
    stockDataService.streamAllCompaniesDetails().forEach(responseObserver::onNext);
    responseObserver.onCompleted();
  }

  @Override
  public void getAllData(Empty request, StreamObserver<SymbolData> responseObserver) {
    stockDataService.streamAllData().forEach(responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
