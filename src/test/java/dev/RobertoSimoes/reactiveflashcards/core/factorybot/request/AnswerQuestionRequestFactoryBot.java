package dev.RobertoSimoes.reactiveflashcards.core.factorybot.request;

import com.github.javafaker.Faker;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.AnswerQuestionRequest;
import lombok.NoArgsConstructor;

import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AnswerQuestionRequestFactoryBot {

    public static AnswerQuestionRequestFactoryBotBuilder builder() {
        return new AnswerQuestionRequestFactoryBotBuilder();
    }

    public static class AnswerQuestionRequestFactoryBotBuilder {
        private String answer;
        private final Faker faker = getFaker();

        public AnswerQuestionRequestFactoryBotBuilder() {
            this.answer = faker.lorem().word();
        }

        public AnswerQuestionRequestFactoryBotBuilder blankAnswer() {
            this.answer = faker.bool().bool() ? null : " ";
            return this;
        }

        public AnswerQuestionRequestFactoryBotBuilder longAnswer() {
            this.answer = faker.lorem().sentence(256);
            return this;
        }

        public AnswerQuestionRequest build() {
            return AnswerQuestionRequest.builder()
                    .answer(answer)
                    .build();
        }
    }
}
