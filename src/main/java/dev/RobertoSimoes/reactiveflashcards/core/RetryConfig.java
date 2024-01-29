package dev.RobertoSimoes.reactiveflashcards.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.time.Duration;

@ConfigurationProperties("retry-cache")
@ConstructorBinding
public record RetryConfig(Long maxRetries, Long minDuration) {
     public Duration minDurationSeconds(){
         return Duration.ofSeconds(minDuration)
     }
}
