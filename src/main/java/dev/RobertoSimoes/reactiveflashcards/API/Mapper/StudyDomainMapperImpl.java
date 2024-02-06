package dev.RobertoSimoes.reactiveflashcards.API.Mapper;

import dev.RobertoSimoes.reactiveflashcards.domain.DTO.QuestionDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.StudyCardDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.StudyDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.Mapper.StudyDomainMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.document.Card;
import dev.RobertoSimoes.reactiveflashcards.domain.document.Question;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyCard;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyDocument;

import java.util.List;
import java.util.Set;

public class StudyDomainMapperImpl implements StudyDomainMapper {
    @Override
    public StudyCard toStudyCard(Card cards) {
        return null;
    }

    @Override
    public Question generateRandomQuestion(Set<StudyCard> cards) {
        return StudyDomainMapper.super.generateRandomQuestion(cards);
    }

    @Override
    public Question toQuestion(StudyCard card) {
        return null;
    }

    @Override
    public QuestionDTO toQuestion(StudyCardDTO card) {
        return null;
    }

    @Override
    public StudyDocument answer(StudyDocument document, String answer) {
        return StudyDomainMapper.super.answer(document, answer);
    }

    @Override
    public StudyDTO toDTO(StudyDocument document, List<String> remainAsks) {
        return null;
    }

    @Override
    public StudyDocument toDocument(StudyDTO dto) {
        return null;
    }

    @Override
    public Object toQuestion(Object o) {
        return null;
    }
}
