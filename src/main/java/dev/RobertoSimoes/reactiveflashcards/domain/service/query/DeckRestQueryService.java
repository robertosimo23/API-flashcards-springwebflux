package dev.RobertoSimoes.reactiveflashcards.domain.service.query;

import dev.RobertoSimoes.reactiveflashcards.core.DeckApiConfig;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.DeckDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class DeckRestQueryService {
    private final WebClient webClient;
    private final DeckApiConfig deckApiConfig;

    public DeckRestQueryService(WebClient webClient, DeckApiConfig deckApiConfig) {
        this.webClient = webClient;
        this.deckApiConfig = deckApiConfig;
    }
    public Flux<DeckDTO> getDecks(){
        return Flux.empty();
    }

}
