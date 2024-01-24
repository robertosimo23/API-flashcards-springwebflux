package dev.RobertoSimoes.reactiveflashcards.domain.service.query;

import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.DECK_NOT_FOUND;

@Slf4j
@Service
@AllArgsConstructor
public class DeckQueryService {

    private final DeckRepository deckRepository;
    public Mono<DeckDocument> findById(final String id){
        return deckRepository.findById(id)
                .doFirst(()-> log.info("\"==== try to find deck with id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(()-> Mono.error(new NotFoundException(DECK_NOT_FOUND.params(id).getMessage()))));

    }

}
