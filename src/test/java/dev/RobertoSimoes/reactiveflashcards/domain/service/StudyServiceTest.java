package dev.RobertoSimoes.reactiveflashcards.domain.service;

import com.github.javafaker.Faker;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.StudyDomainMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.MailMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.MailMapperImpl_;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.Mapper.MailMapperDecorator;
import dev.RobertoSimoes.reactiveflashcards.domain.Mapper.StudyDomainMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyCard;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyDeck;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.DeckInStudyException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.StudyRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudyServiceTest {
    @Mock
    private MailService mailService;
    @Mock
    private UserQueryService userQueryService;
    @Mock
    private DeckQueryService deckQueryService;
    @Mock
    private StudyQueryService studyQueryService;
    @Mock
    private StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper = new StudyDomainMapperImpl();
    private final MailMapperDecorator mailMapper = new MailMapperImpl(new MailMapperImpl_());
    private StudyService studyService;
    private final static Faker faker = getFaker();

    @BeforeEach
    void setup() {
        studyService = new StudyService(mailService, userQueryService, deckQueryService, studyQueryService
                , studyRepository, studyDomainMapper, mailMapper);

    }

    @Test
    void starTest() {
        var deck = DeckDocumentFactoryBot.builder().build();
        var user = UserDocumentFactoryBot.builder().build();
        when(studyQueryService.findPendingStudyByUserIdAndDeckId(anyString(), anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(user));
        when(deckQueryService.findById(anyString())).thenReturn(Mono.just(deck));
        when(studyRepository.save(any(StudyDocument.class))).thenAnswer(invocation -> {
            var study = invocation.getArgument(0, StudyDocument.class);
            return Mono.just(study.toBuilder()
                    .id(ObjectId.get().toString())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build());

        });
        var studyDeck = StudyDeck.builder().deckId(deck.id()).build();
        var study = StudyDocument.builder()
                .userId(user.id())
                .studyDeck(studyDeck)
                .build();
        StepVerifier.create(studyService.start(study))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.userId()).isEqualTo(user.id());
                    assertThat(actual.complete()).isFalse();
                    assertThat(actual.studyDeck().deckId()).isEqualTo(deck.id());
                    assertThat(actual.studyDeck().cards())
                            .containsExactlyInAnyOrderElementsOf(deck.cards().stream().map(c -> StudyCard.builder()
                                            .front(c.front())
                                            .back(c.back())
                                            .build())
                                    .collect(Collectors.toSet()));
                    assertThat(actual.questions().size()).isOne();
                    var question = actual.questions().get(0);
                    assertThat(actual.studyDeck().cards()).contains(StudyCard.builder()
                            .front(question.asked())
                            .back(question.expected())
                            .build());
                    assertThat(question.isAnswered()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    void whenUserTryToStartStudyWithDeckTwoTimesThenThrowError() {
        var deck = DeckDocumentFactoryBot.builder().build();
        when(studyQueryService.findPendingStudyByUserIdAndDeckId(anyString(), anyString()))
                .thenReturn(Mono.just(StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck).build()));
        var studyDeck = StudyDeck.builder().deckId(deck.id()).build();
        var study = StudyDocument.builder()
                .userId(ObjectId.get().toString())
                .studyDeck(studyDeck)
                .build();
        StepVerifier.create(studyService.start(study))
                .verifyError(DeckInStudyException.class);
    }

        @Test
        void whenNonStoredUserTryToStartStudyThenThrowError(){
            var deck = DeckDocumentFactoryBot.builder().build();
            var user = UserDocumentFactoryBot.builder().build();
            when(studyQueryService.findPendingStudyByUserIdAndDeckId(anyString(), anyString()))
                    .thenReturn(Mono.error(new NotFoundException("")));
            when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));

            var studyDeck = StudyDeck.builder().deckId(deck.id()).build();
            var study = StudyDocument.builder()
                    .userId(user.id())
                    .studyDeck(studyDeck)
                    .build();
            StepVerifier.create(studyService.start(study)).verifyError(NotFoundException.class);
        }

}
