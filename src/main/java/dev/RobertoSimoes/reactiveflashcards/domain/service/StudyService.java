package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.API.Mapper.StudyDomainMapper;
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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.DECK_IN_STUDY;

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
                .flatMap(study -> Mono.defer(() -> Mono.error(new DeckInStudyException(DECK_IN_STUDY.params(document.userId(), document.studyDeck().deckId().getMessage()))))
                        .onErrorResume(NotFoundException.class, e -> Mono.empty())
                        .then();

    }

    public Mono<StudyDocument> answer(final String id, String answer) {
        studyQueryService.findById(id)
                .flatMap(studyQueryService::verifyIfFinished)
                .map(study -> studyDomainMapper.answer(study, answer));
        return Mono.just(StudyDocument.builder().build());
    }

    private Flux<String> getNotAnswer(Set<StudyCard> cards, List<Question> questions) {
        return getCardAnswers(cards)
                .filter(ask -> questions.stream()
                        .filter(Question::isCorrect).map(Question::asked)
                        .anyMatch(q -> q.equals(ask)));

    }

    private Flux<String> getCardAnswers(final Set<StudyCard> cards) {
        return Flux.fromIterable(cards)
                .map(StudyCard::front);

    }

    private Mono<List<String>> getAsked(final Set<Question> questions) {
        return Flux.fromIterable(questions)
                .filter(Question::isCorrect)
                .map(Question::asked)
                .collectList();
    }
}

}
