mn create-grpc-app --build=maven --jdk=17 --lang=java --test=junit --features=lombok,mockito,assertj,tracing-zipkin,logback pl.jcw.example.grpc.grpc-stock-quotes


Create service:
```shell
mn create-grpc-app --build=gradle --jdk=17 --lang=java --test=junit \
  --features=lombok,mockito,assertj,logback \
  pl.jcw.example.grpc.grpc-stock-data-hub
```
