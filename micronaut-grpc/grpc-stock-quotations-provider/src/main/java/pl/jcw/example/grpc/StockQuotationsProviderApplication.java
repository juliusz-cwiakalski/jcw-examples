package pl.jcw.example.grpc;

import io.micronaut.runtime.Micronaut;

public class StockQuotationsProviderApplication {
  public static void main(String[] args) {
    Micronaut.build(args)
        .eagerInitSingletons(true)
        .mainClass(StockQuotationsProviderApplication.class)
        .start();
  }
}
