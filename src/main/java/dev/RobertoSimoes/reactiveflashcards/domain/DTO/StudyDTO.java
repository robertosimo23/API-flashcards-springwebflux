package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import dev.RobertoSimoes.reactiveflashcards.domain.document.Question;
import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyDeck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.collections.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


public record StudyDTO(
        String id,
        String userId,
        Boolean complete,
        StudyDeck studyDeck,
        List<QuestionDTO> questions,
        List<String> remainAnswers,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static StudyDocumentBuilder builder() {
        return new StudyDocumentBuilder();
    }

    public StudyDocumentBuilder toBuilder() {
        return new StudyDocumentBuilder(id, userId, studyDeck, questions, remainAnswers, createdAt, updatedAt);
    }

    public void addQuestion(final Question question) {
        questions.add(question);
    }


    public Boolean hasAnyAnswer() {
        return CollectionUtils.isNotEmpty(remainAnswers);
    }

    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class StudyDocumentBuilder {


        private String id;
        private String userId;
        private StudyDeckDTO studyDeck;
        private List<Question> questions = new ArrayList<>();
        private List<String> remainAnswers;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public StudyDocumentBuilder() {

        }

        public StudyDocumentBuilder id(final String id) {
            this.id = id;
            return this;

        }

        public StudyDocumentBuilder userId(final String userId) {
            this.userId = userId;
            return this;
        }


        public StudyDocumentBuilder studyDeck(final StudyDeck studyDeck) {
            this.studyDeck = studyDeck;
            return this;
        }

        public StudyDocumentBuilder questions(final List<QuestionDTO> questions) {
            this.questions = questions;
            return this;
        }

        public StudyDocumentBuilder remainAnswers(final List<String> remainAnswers) {
            this.remainAnswers = remainAnswers;
            return this;
        }

        public StudyDocumentBuilder question(final QuestionDTO question) {
            this.questions.add(question);
            return this;
        }

        public StudyDocumentBuilder createdAt(final OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudyDocumentBuilder updatedAt(final OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StudyDTO build() {
            var rightQuestions = questions.stream().filter(QuestionDTO::isCorrect).toList();
            var complete = rightQuestions.size() == studyDeck.cards.size();
            return new StudyDTO(id, userId, complete, studyDeck, questions,remainAnswers, createdAt, updatedAt);
        }
    }
}

