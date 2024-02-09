package dev.RobertoSimoes.reactiveflashcards.api.controller.study;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.StudyController;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ErrorFieldResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.QuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.StudyDomainMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.controller.AbstractControllerTest;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.service.StudyQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.StudyService;
import dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.questionResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {StudyDomainMapperImpl.class})
@WebFluxTest(StudyController.class)
public class StudyControllerCurrentQuestionTest extends AbstractControllerTest {
    @MockBean
    private StudyService studyService;
    @MockBean
    private StudyQueryService studyQueryService;
    private RequestBuilder<QuestionResponse> questionResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;
    @BeforeEach
    void setup(){
        questionResponseRequestBuilder = questionResponseRequestBuilder(applicationContext, "/studies");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/studies");
    }
    @Test
    void getCurrentQuestion(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var question = StudyDocumentFactoryBot.builder(ObjectId.get().toString(),deck)
                .pendingQuestions(1)
                .build().getLastPendingQuestion();
        when(studyQueryService.getLastPendingQuestion(anyString())).thenReturn(Mono.just(question));
        questionResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                .pathSegment("{id}")
                .pathSegment("current-question")
                .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsOk()
                .assertBody(response -> assertThat(response).isNotNull());
    }
    @Test
    void whenTryToGetQuestionFromNonStoredStudyThenReturnNotFound(){
        when(studyQueryService.getLastPendingQuestion(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        problemResponseRequestBuilder.uri(uriBuilder-> uriBuilder
                .pathSegment("{id }")
                .pathSegment("current-question")
                .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusNotFound()
                .assertBody(actual->{
                   assertThat(actual).isNotNull();
                   assertThat(actual.status()).isEqualTo(NOT_FOUND.value());
                });
    }
    @Test
    void whenTryUseInvalidIdThenReturnBadRequest(){
        when(studyQueryService.getLastPendingQuestion(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        problemResponseRequestBuilder.uri(uriBuilder-> uriBuilder
                        .pathSegment("{id }")
                        .pathSegment("current-question")
                        .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsBadRequest()
                .assertBody(actual->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains("id");
                });
    }
}
