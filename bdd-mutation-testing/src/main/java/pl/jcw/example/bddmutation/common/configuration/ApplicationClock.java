package pl.jcw.example.bddmutation.common.configuration;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApplicationClock {

  @Bean
  Clock clock() {
    return Clock.systemDefaultZone();
  }
}
