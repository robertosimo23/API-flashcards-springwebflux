package dev.RobertoSimoes.reactiveflashcards.domain.service.query;

import dev.RobertoSimoes.reactiveflashcards.core.factorybot.dto.DeckDTOFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.DeckRepository;
import dev.RobertoSimoes.reactiveflashcards.domain.service.DeckQueryService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.stream.Stream;

import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DeckQueryServiceTest {

    @Mock
    private DeckRepository deckRepository;
    private DeckQueryService deckQueryService;

    @BeforeEach
    void setup(){
        deckQueryService = new DeckQueryService(deckRepository);

    }
    @Test
    void findAllTest(){
        var faker = getFaker();
        var documents = Stream.generate(()-> DeckDTOFactoryBot.builder().build())
                .limit(faker.number().randomDigitNotZero())
                .toList();
        when(deckRepository.findAll()).thenReturn(Flux.fromIterable(documents));
        StepVerifier.create(deckQueryService.findAll())
                .recordWith(ArrayList::new)
                .thenConsumeWhile(document -> true)
                .consumeRecordedWith(actual -> assertThat(actual.size()).isEqualTo(documents.size()))
                .verifyComplete();
        verify(deckRepository.findAll());
    }
    @Test
    void findByIdTest(){
        var document = DeckDTOFactoryBot.builder().build();
        when(deckRepository.findById(any(String.class))).thenReturn(Mono.just(document));
        StepVerifier.create(deckQueryService.findById(ObjectId.get().toString()))
                .assertNext(actual -> assertThat(actual).isNotNull())
                .verifyComplete();
        verify(deckRepository).findById(any(String.class));
    }
    @Test
    void whenTryToFindNonStoredDeckThenThrowError(){
        when(deckRepository.findById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(deckQueryService.findById(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(deckRepository).findById(anyString());
    }
}
