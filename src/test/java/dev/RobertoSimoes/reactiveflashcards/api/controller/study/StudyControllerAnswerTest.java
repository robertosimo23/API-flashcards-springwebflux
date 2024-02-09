package dev.RobertoSimoes.reactiveflashcards.api.controller.study;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.StudyController;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.AnswerQuestionRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.AnswerQuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ErrorFieldResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.StudyMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.controller.AbstractControllerTest;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.request.AnswerQuestionRequestFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.service.StudyQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.StudyService;
import dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.answerQuestionResponseRequestBuilder;
import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {StudyMapperImpl.class})
@WebFluxTest(StudyController.class)
public class StudyControllerAnswerTest extends AbstractControllerTest {
    @MockBean
    private StudyService studyService;
    @MockBean
    private StudyQueryService studyQueryService;

    private RequestBuilder<AnswerQuestionResponse> answerQuestionResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup() {
        answerQuestionResponseRequestBuilder = answerQuestionResponseRequestBuilder(applicationContext, "/studies");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/studies");
    }

    @Test
    void answerTest() {
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck)
                .pendingQuestions(1)
                .build();
        var request = AnswerQuestionRequestFactoryBot.builder().build();
        when(studyService.answer(anyString(), anyString())).thenReturn(Mono.just(study));
        answerQuestionResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("answer")
                        .build(study.id()))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsOk()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    var currentQuestion = study.getLastAnsweredQuestions();
                    assertThat(response.answered()).isEqualTo(currentQuestion.answered());
                    assertThat(response.answeredIn().toEpochSecond()).isEqualTo(currentQuestion.answeredIn().toEpochSecond());
                    assertThat(response.expected()).isEqualTo(currentQuestion.expected());
                });
    }

    @Test
    void whenNotFoundStudyOrStudyIsFinishedThenReturnNotFound() {
        var request = AnswerQuestionRequestFactoryBot.builder().build();
        when(studyService.answer(anyString(), anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("answer")
                        .build(ObjectId.get().toString()))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusNotFound()
                .assertBody(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    private static Stream<Arguments> checkConstraintTest() {
        return Stream.of(
                Arguments.of(AnswerQuestionRequestFactoryBot.builder().build(),
                        faker.lorem().word(), "id"),
                Arguments.of(AnswerQuestionRequestFactoryBot.builder().blankAnswer().build(),
                        ObjectId.get().toString(), "answer"),
                Arguments.of(AnswerQuestionRequestFactoryBot.builder().longAnswer().build(), ObjectId.get().toString(),
                        "answer")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkConstraintTest(final AnswerQuestionRequest request,
                             final String studyId,
                             final String field) {
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("answer")
                        .build(studyId))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsBadRequest()
                .assertBody(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains(field);
                });

    }
}