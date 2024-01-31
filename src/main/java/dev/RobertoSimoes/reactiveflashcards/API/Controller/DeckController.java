package dev.RobertoSimoes.reactiveflashcards.API.Controller;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.documentation.DeckControllerDoc;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.DeckRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.DeckResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.DeckMapper;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import dev.RobertoSimoes.reactiveflashcards.domain.service.DeckService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.DeckQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController implements DeckControllerDoc {

    public final DeckService deckService;
    public final DeckQueryService deckQueryService;
    public final DeckMapper deckMapper;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest request) {
        return deckService.save(deckMapper.toDocument(request))
                .doFirst(() -> log.info("=== Saving a deck with follow data {}", request))
                .map(deckMapper::toResponse);
    }

    @Override
    @PostMapping(value = "sync")
    public Mono<Void> sync() {
        return deckService.sync();
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> findById(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id) {
        return deckQueryService.findById(id)
                .doFirst(() -> log.info("==== Finding a deck with a follow id{}", id))
                .map(deckMapper::toResponse);
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<DeckResponse> findAll() {
        return deckQueryService.findAll()
                .doFirst(() -> log.info("==== Finding all decks"))
                .map(deckMapper::toResponse);
    }

    @Override
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> update(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id,
                                     @Valid @RequestBody final DeckRequest request) {
        return deckService.update(deckMapper.toDocument(request, id))
                .doFirst(() -> log.info("==== Updating a deck with follow info [body: {}, id: {}]", request, id))
                .map(deckMapper::toResponse);
    }


    @Override
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id) {
        return deckService.delete(id)
                .doFirst(() -> log.info("==== Deleting a user with follow id {}", id));
    }

}
