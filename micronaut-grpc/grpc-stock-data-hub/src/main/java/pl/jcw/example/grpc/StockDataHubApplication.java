package pl.jcw.example.grpc;

import io.micronaut.runtime.Micronaut;

public class StockDataHubApplication {
  public static void main(String[] args) {
    Micronaut.build(args)
        .eagerInitSingletons(true)
        .mainClass(StockDataHubApplication.class)
        .start();
  }
}
