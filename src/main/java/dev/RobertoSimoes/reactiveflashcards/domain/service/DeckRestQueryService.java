package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.core.DeckApiConfig;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.AuthDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.DeckDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
public class DeckRestQueryService {
    private final WebClient webClient;
    private final DeckApiConfig deckApiConfig;
    public final Mono<AuthDTO> authCache;


    public DeckRestQueryService(WebClient webClient, DeckApiConfig deckApiConfig) {
        this.webClient = webClient;
        this.deckApiConfig = deckApiConfig;
        authCache = Mono.from(getAuth())
                .cache(auth -> Duration.ofSeconds(Long.parseLong(auth.expiresIn()) - 5),
                        throwable -> Duration.ZERO,
                        () -> Duration.ZERO);
    }

    public Flux<DeckDTO> getDecks() {
        return authCache
                .flatMapMany(auth -> doGetDecks(auth.token()));


    }

    private Flux<DeckDTO> doGetDecks(final String token) {
        return webClient.get()
                .uri(deckApiConfig.getDecksUri())
                .header("token", token)
                .retrieve()
                .bodyToFlux(DeckDTO.class);
    }

    public Mono<AuthDTO> getAuth() {
        return webClient.post()
                .uri(deckApiConfig.getAuthUri())
                .contentType(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AuthDTO.class);
    }
}
