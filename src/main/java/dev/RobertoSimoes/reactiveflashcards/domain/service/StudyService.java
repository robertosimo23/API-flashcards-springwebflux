package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.API.Mapper.StudyDomainMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.QuestionDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.StudyDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.document.Card;
import dev.RobertoSimoes.reactiveflashcards.domain.document.Question;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyCard;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.DeckInStudyException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.StudyRepository;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.DeckQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.StudyQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.DECK_IN_STUDY;
import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.STUDY_QUESTION_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {

    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyQueryService studyQueryService;
    private final StudyDomainMapper studyDomainMapper;
    private final StudyRepository studyRepository;


    public Mono<StudyDocument> star(final StudyDocument document) {
        return verifyStudy(document)
                .then(userQueryService.findbyId(document.userId()))
                .flatMap(user -> deckQueryService.findById(document.studyDeck().deckId()))
                .flatMap(deck -> fillDeckStudyCards(document, deck.cards()))
                .map(study -> study.toBuilder().question(studyDomainMapper.gerenateRandomQuestion(study.studyDeck().cards()))
                        .build())
                .doFirst(() -> log.info("==== generating a first random question "))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("a follow study was save {}", study));

    }

    public Mono<StudyDocument> fillDeckStudyCards(final StudyDocument document, final Set<Card> cards) {
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("==== copy cards to new study"))
                .map(studyDomainMapper::toStudyCard)
                .collectList()
                .map(studyCards -> document.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> document.toBuilder().studyDeck(studyDeck).build());


    }

    private Mono<Void> verifyStudy(final StudyDocument document) {
        return studyQueryService.findPendingStudyByUserIdAndDeckId(document.userId(), document.studyDeck().deckId())
                .flatMap(study -> Mono.defer(() -> Mono.error(new DeckInStudyException(DECK_IN_STUDY.params(document.userId(), document.studyDeck().deckId()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();

    }

    public Mono<StudyDocument> answer(final String id, String answer) {
        return studyQueryService.findById(id)
                .flatMap(studyQueryService::verifyIfFinished)
                .map(study -> studyDomainMapper.answer(study, answer))
                .zipWhen(this::getNextPossibilities)
                .map(tuple -> studyDomainMapper.toDTO(tuple.getT1(), tuple.getT2()))
                .flatMap(this::setNewQuestions)
                .map(studyDomainMapper::toDocument)
                .flatMap(studyRepository::save)
                .doFirst(() -> log.info("==== saving answer and next question if have one "));
    }

    private Mono<List<String>> getNextPossibilities(final StudyDocument document) {
        return Flux.fromIterable(document.studyDeck().cards())
                .doFirst(() -> log.info("==== Getting question not used or questions without right answers"))
                .map(StudyCard::front)
                .filter(asks -> document.questions().stream()
                        .filter(Question::isCorrect)
                        .map(Question::asked)
                        .noneMatch(q -> q.equals(asks)))
                .collectList()
                .flatMap(asks -> removeLastAsk(asks, document.getLastAnsweredQuestions().asked()));
    }

    private Mono<List<String>> removeLastAsk(List<String> asks, String asked) {
        return Mono.just(asks)
                .doFirst(()-> log.info("==== remove last asked question if it is not a last pending question study"))
                .filter(a -> a.size() == 1)
                .switchIfEmpty(Mono.defer(() -> Mono.just(asks.stream()
                        .filter(a -> a.equals(asked))
                        .collect(Collectors.toList()))));
    }


    private Mono<List<String>> getAsked(final Set<Question> questions) {
        return Flux.fromIterable(questions)
                .filter(Question::isCorrect)
                .map(Question::asked)
                .collectList();
    }

    private Mono<StudyDTO> setNewQuestions(final StudyDTO dto) {
        return Mono.just(dto.hasAnyAnswer())
                .filter(BooleanUtils::isTrue)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND
                        .params(dto.id())
                        .getMessage()))))
                .flatMap(hasAnyAnswer -> generateNextQuestions(dto))
                .map(question -> dto.toBuilder().question(question).build())
                .onErrorResume(NotFoundException.class, e -> Mono.just(dto));
    }

    private Mono<QuestionDTO> generateNextQuestions(final StudyDTO dto) {
        return Mono.just(dto.remainAsks().get(new Random().nextInt(dto.remainAsks().size())))
                .doFirst(()-> log.info("==== select next random question"))
                .map(ask -> dto.studyDeck()
                        .cards()
                        .stream()
                        .filter(card -> card.front().equals(ask))
                        .map(studyDomainMapper::toQuestion)
                        .findFirst()
                        .orElseThrow());


    }
}

