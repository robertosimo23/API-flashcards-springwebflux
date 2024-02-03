package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.domain.document.Question;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.STUDY_NOT_FOUND;
import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.STUDY_QUESTION_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class StudyQueryService {
    private final StudyRepository studyRepository;

    public Mono<StudyDocument> findPendingStudyByUserIdAndDeckId(final String userId, final String deckId) {
        return studyRepository.findByUserIdAndCompleteFalseAndStudyDeck_DeckId(userId, deckId)
                .doFirst(() -> log.info(" ==== Try to get pending study with userId{} and deckId {}", userId, deckId))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NotFoundException(STUDY_NOT_FOUND.params(userId, deckId).getMessage()))));

    }

    public Mono<StudyDocument> findById(final String id) {
        return studyRepository.findById(id)
                .doFirst(()-> log.info("=== Getting a study with id{}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_NOT_FOUND.params(id).getMessage()))));

    }



    public Mono<StudyDocument> verifyIfFinished(final StudyDocument document){
        return Mono.just(document)
                .doFirst(()-> log.info("==== verify if study has some question right answer"))
                .filter(study-> BooleanUtils.isFalse(document.complete()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND
                .params(document.id()).getMessage()))));
    }
    public Mono<Question> getLastPendingQuestion(final String id) {
        return findById(id)
                .flatMap(this:: verifyIfFinished)
                .flatMapMany(study -> Flux.fromIterable(study.questions()))
                .filter(Question::isAnswered)
                .doFirst(()-> log.info("=== Getting a current pending question in study{}",id))
                .single();
    }
}
