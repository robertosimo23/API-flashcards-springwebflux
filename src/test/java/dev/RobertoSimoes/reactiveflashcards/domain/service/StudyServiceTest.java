package dev.RobertoSimoes.reactiveflashcards.domain.service;

import com.github.javafaker.Faker;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.StudyDomainMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.MailMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.MailMapperImpl_;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.MailMessageDTO;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;
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
    void whenNonStoredUserTryToStartStudyThenThrowError() {
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

    @Test
    void whenHasNonStudyStoredThenThrowError() {
        when(studyQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));

        StepVerifier.create(studyService.answer(ObjectId.get().toString(), faker.lorem().word()))
                .verifyError(NotFoundException.class);
    }

    @Test
    void whenStudyHasNonPendingQuestionsThenThrowError() {
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck).build();
        when(studyQueryService.findById(anyString())).thenReturn(Mono.just(study));
        when(studyQueryService.verifyIfFinished(any(StudyDocument.class))).thenReturn(Mono.error(new NotFoundException(" ")));

        StepVerifier.create(studyService.answer(ObjectId.get().toString(), faker.lorem().word()))
                .verifyError(NotFoundException.class);
    }

    private static Stream<Arguments> answerTest() {
        var user = UserDocumentFactoryBot.builder().build();
        var deck = DeckDocumentFactoryBot.builder().build();
        var oneRemainQuestion = StudyDocumentFactoryBot.builder(user.id(), deck).pendingQuestions(1).build();
        Consumer<StudyDocument> nextSameQuestion = actual -> {
            var question = actual.getLastPendingQuestion();
            assertThat(question.asked()).isEqualTo(actual.getLastAnsweredQuestions().asked());
            assertThat(question).isNotNull();
            assertThat(question.asked()).isNotEqualTo(actual.getLastAnsweredQuestions().asked());
        };
        var twoRemainQuestion = StudyDocumentFactoryBot.builder(user.id(), deck).pendingQuestions(2).build();
        Consumer<StudyDocument> nextDiffQuestion = actual -> {
            var question = actual.getLastPendingQuestion();
            assertThat(question).isNotNull();
            assertThat(question.asked()).isNotEqualTo(actual.getLastAnsweredQuestions().asked());
        };
        return Stream.of(
                Arguments.of(oneRemainQuestion, faker.lorem().word(), nextSameQuestion),
                Arguments.of(twoRemainQuestion, faker.lorem().word(), nextDiffQuestion)
        );
    }

    @ParameterizedTest
    @MethodSource
    void answerTest(final StudyDocument study, final String answer, final Consumer<StudyDocument> asserts) {
        when(studyQueryService.findById(anyString())).thenReturn(Mono.just(study));
        when(studyQueryService.verifyIfFinished(any(StudyDocument.class))).thenReturn(Mono.just(study));
        when(studyRepository.save(any(StudyDocument.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, StudyDocument.class);
            return Mono.just(document.toBuilder().updatedAt(OffsetDateTime.now()).build());
        });

        StepVerifier.create(studyService.answer(study.id(), answer))
                .assertNext(asserts)
                .verifyComplete();
    }

    @Test
    void whenStudyIsFinishedThenSendEmail() throws InterruptedException {
        var user = UserDocumentFactoryBot.builder().build();
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(user.id(), deck).pendingQuestions(1).build();
        var mailCaptor = ArgumentCaptor.forClass(MailMessageDTO.class);
        when(studyQueryService.findById(anyString())).thenReturn(Mono.just(study));
        when(studyQueryService.verifyIfFinished(any(StudyDocument.class))).thenReturn(Mono.just(study));
        when(studyRepository.save(any(StudyDocument.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, StudyDocument.class);
            return Mono.just(document.toBuilder().updatedAt(OffsetDateTime.now()).build());
        });
        when(mailService.send(mailCaptor.capture())).thenReturn(Mono.empty());

        var answer = deck.cards().stream().filter(c -> study.getLastPendingQuestion().asked().equals(c.front()))
                .findFirst().orElseThrow().back();
        StepVerifier.create(studyService.answer(study.id(), answer))
                .assertNext(actual -> assertThat(actual.complete()).isTrue())
                .verifyComplete();
        TimeUnit.SECONDS.sleep(2);

    }

}

