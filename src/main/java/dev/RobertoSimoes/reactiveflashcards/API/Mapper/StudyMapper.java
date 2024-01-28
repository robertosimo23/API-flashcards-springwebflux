package dev.RobertoSimoes.reactiveflashcards.API.Mapper;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.StudyRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.AnswerQuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.QuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.domain.document.Question;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studyDeck.deckId", source = "deckId")
    @Mapping(target = "studyDeck.cards", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StudyDocument toDocument(final StudyRequest request);

    QuestionResponse toResponse(final Question question, final String id);

    AnswerQuestionResponse toResponse(final Question question);
}
