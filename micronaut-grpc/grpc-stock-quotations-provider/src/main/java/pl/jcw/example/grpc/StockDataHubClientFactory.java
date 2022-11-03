package pl.jcw.example.grpc;

import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;
import jakarta.inject.Singleton;

@Factory
public class StockDataHubClientFactory {

  @Singleton
  StockDataServiceGrpc.StockDataServiceStub reactiveStub(
      @GrpcChannel("stock-data-hub") ManagedChannel channel) {
    return StockDataServiceGrpc.newStub(channel);
  }
}
