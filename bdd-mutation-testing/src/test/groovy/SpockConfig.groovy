runner {
  parallel {
    enabled true
    fixedParallelism Runtime.runtime.availableProcessors().intdiv(1.5) ?: 1
    keepAliveSeconds 60
    maxPoolSize Runtime.runtime.availableProcessors().intdiv(1.5) ?: 1
    corePoolSize Runtime.runtime.availableProcessors().intdiv(1.5) ?: 1
  }
}
