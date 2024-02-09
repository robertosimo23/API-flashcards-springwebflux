package dev.RobertoSimoes.reactiveflashcards.api.controller.user;

import com.github.javafaker.Faker;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.UserController;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ErrorFieldResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import dev.RobertoSimoes.reactiveflashcards.api.controller.AbstractControllerTest;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.service.UserQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.UserService;
import dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import reactor.core.publisher.Mono;

import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.noContentRequestBuilder;
import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@WebFluxTest(UserController.class)
public class UserControllerDeleteTest extends AbstractControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserQueryService userQueryService;

    private RequestBuilder<Void> noContentRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup() {
        noContentRequestBuilder = noContentRequestBuilder(applicationContext, "/users");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/users");
    }

    @Test
    void deleteTest() {
        when(userService.delete(anyString())).thenReturn(Mono.empty());
        noContentRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .generateRequestWithoutBody()
                .doDelete()
                .httpStatusIsNoContent();

    }

    @Test
    void whenTryToDeleteNoStoredUserThenReturnNotFound() {
        when(userService.delete(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .httpStatusNotFound()
                .assertBody(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    @Test
    void whenTryUseInvalidIdThenReturnBadRequest() {
        when(userService.delete(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .httpStatusIsBadRequest()
                .assertBody(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains("id");

                });

    }

}
