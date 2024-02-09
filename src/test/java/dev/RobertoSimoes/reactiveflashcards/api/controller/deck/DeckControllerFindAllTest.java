package dev.RobertoSimoes.reactiveflashcards.api.controller.deck;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.DeckController;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.DeckResponse;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.DeckMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.controller.AbstractControllerTest;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.service.DeckQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.DeckService;
import dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.deckResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {DeckMapperImpl.class})
@WebFluxTest(DeckController.class)
public class DeckControllerFindAllTest extends AbstractControllerTest {
    @MockBean
    public DeckService deckService;
    @MockBean
    public DeckQueryService deckQueryService;
    private RequestBuilder<DeckResponse> deckResponseRequestBuilder;


    @BeforeEach
    void setup() {
        deckResponseRequestBuilder = deckResponseRequestBuilder(applicationContext, "decks");
    }

    private static Stream<Arguments> findAllTest() {
        var users = Stream.generate(() -> DeckDocumentFactoryBot.builder().build())
                .limit(faker.number().randomDigitNotZero())
                .toList();
        Consumer<List<DeckResponse>>assertNonNull= deckResponse -> {
            assertThat(deckResponse).isNotNull();
            assertThat(deckResponse).isNotEmpty();
        };
        Consumer<List<DeckResponse>> assertNull = deckResponse -> {
            assertThat(deckResponse).isNotNull();
            assertThat(deckResponse).isEmpty();
        };
        return Stream.of(
                Arguments.of(Flux.fromIterable(users), assertNonNull),
                Arguments.of(Flux.fromIterable(new ArrayList<>()), assertNull)
        );
    }

    @ParameterizedTest
    @MethodSource
    void findAllTest(final Flux<DeckDocument> deckDocumentMock, final Consumer<List<DeckResponse>> deckAssets) {
        when(deckQueryService.findAll()).thenReturn(deckDocumentMock);
        deckResponseRequestBuilder.uri(UriBuilder::build)
                .generateRequestWithCollectionBody()
                .doGet()
                .httpStatusIsOk()
                .assertBody(deckAssets);
    }
}
