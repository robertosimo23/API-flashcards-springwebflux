package dev.RobertoSimoes.reactiveflashcards.api.controller.deck;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.DeckController;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.DeckResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ErrorFieldResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.DeckMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.controller.AbstractControllerTest;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.service.DeckQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.DeckService;
import dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.deckResponseRequestBuilder;
import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {DeckMapperImpl.class})
@WebFluxTest(DeckController.class)
public class DeckControllerFindByIdTest extends AbstractControllerTest {
    @MockBean
    public DeckService deckService;
    @MockBean
    public DeckQueryService deckQueryService;
    private RequestBuilder<DeckResponse> deckResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup() {
        deckResponseRequestBuilder = deckResponseRequestBuilder(applicationContext, "/decks");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/decks");
    }

    @Test
    void findByIdTest() {
        var deck = DeckDocumentFactoryBot.builder().build();
        when(deckQueryService.findById(anyString())).thenReturn(Mono.just(deck));
        deckResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(deck.id()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsOk()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response).usingRecursiveComparison()
                            .ignoringFields("createdAt", "updatedAt")
                            .isEqualTo(deck);
                });
    }

    @Test
    void whenTryToFindNonStoredDeckThenReturnNotFound() {
        when(deckQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusNotFound()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    @Test
    void whenTryUseInvalidIdThenReturnBadRequest() {
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsBadRequest()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(response.fields().stream().map(ErrorFieldResponse::name).toList()).contains("id");
                });
    }
}
