package pl.jcw.example.grpc;

import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;
import jakarta.inject.Singleton;
import pl.jcw.example.grpc.PerformanceTestingSinkServiceGrpc.PerformanceTestingSinkServiceStub;

@Factory
public class PerformanceTestingSinkServiceStubFactory {

  @Singleton
  PerformanceTestingSinkServiceStub reactiveStub(
      @GrpcChannel("stock-data-hub") ManagedChannel channel) {
    return PerformanceTestingSinkServiceGrpc.newStub(channel).withWaitForReady();
  }
}
