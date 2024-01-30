package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.domain.Mapper.DeckDoMainMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.DeckRepository;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.DeckQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.DeckRestQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;
    private final DeckQueryService deckQueryService;
    private final DeckRestQueryService deckRestQueryService;
    private final DeckDoMainMapper deckDoMainMapper;


    public Mono<DeckDocument> save(final DeckDocument document) {
        return deckRepository.save(document)
                .doFirst(() -> log.info("=== try to save a follow document{}", document));
    }

    public Mono<DeckDocument> update(final DeckDocument document) {
        return deckQueryService.findById(document.id())
                .map(deck -> document.toBuilder()
                        .createdAt(document.createdAt())
                        .updatedAt(document.updatedAt())
                        .build())
                .flatMap(deckRepository::save)
                .doFirst(() -> log.info("==== Try to update a deck with follow info {}", document));
    }

    public Mono<Void> delete(final String id) {
        return deckQueryService.findById(id)
                .flatMap(deckRepository::delete)
                .doFirst(() -> log.info("==== Try to delete a user with follow id {}", id));
    }
        public Mono<Void> sync(){
        return Mono.empty()
                .onTerminateDetach()
                .doOnSuccess(a -> backgroundSync())
                .then();
        }

    private void backgroundSync() {
        deckRestQueryService.getDecks()
                .map(deckDoMainMapper::toDocument)
                .flatMap(deckRepository::save)
                .then()
                .subscribe();
    }

}
