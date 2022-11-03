# Overview

This is example project demonstrating gRPC communication between Micronaut based services.
It also presents how Nginx may be used as proxy/gateway.

# Running locally with docker

```shell
git clone https://github.com/juliusz-cwiakalski/examples.git
cd examples/micronaut-grpc
docker-compose up
```

gRPC services definitions: [micronaut-grpc/grpc-stock-data-hub/src/main/proto/grpcStockData.proto](./grpc-stock-data-hub/src/main/proto/grpcStockData.proto)

Ports:
- 40050 - Nginx API Gateway
- 40051 - Stock data hub service, implements gRPC services: 
  - StockDataService
  - PerformanceTestingSinkService
- 40052 - Quotations provider service, implements gRPC service:
  - PerformanceTestingTriggerService

# Notes

Create gRPC Micronaut service:
```shell
mn create-grpc-app --build=gradle --jdk=17 --lang=java --test=junit \
  --features=lombok,mockito,assertj,logback \
  pl.jcw.example.grpc.grpc-stock-data-hub
```

# Useful resources

- [Article about JWT authentication with gRPC](https://sultanov.dev/blog/securing-java-grpc-services-with-jwt-based-authentication/)
- [How to make nginx.conf out of env variables](https://serverfault.com/a/919212/475920)
