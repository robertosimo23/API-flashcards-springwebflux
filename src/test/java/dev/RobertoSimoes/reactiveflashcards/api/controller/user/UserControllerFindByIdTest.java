package dev.RobertoSimoes.reactiveflashcards.api.controller.user;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.UserController;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.UserResponse;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.UserMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.controller.AbstractControllerTest;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.service.UserQueryService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.UserService;
import dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static dev.RobertoSimoes.reactiveflashcards.utils.request.RequestBuilder.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {UserMapperImpl.class})
@WebFluxTest(UserController.class)
public class UserControllerFindByIdTest extends AbstractControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserQueryService userQueryService;
    private RequestBuilder<UserResponse> userResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup() {
        userResponseRequestBuilder = userResponseRequestBuilder(applicationContext, "/users");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/users");
    }

    @Test
    void findByIdTest() {
        var user = UserDocumentFactoryBot.builder().build();
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(user));
        userResponseRequestBuilder
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(user.id()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsOk()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response).usingRecursiveComparison()
                            .ignoringFields("createdAt", "updateAt")
                            .isEqualTo(user);
                });

    }

    @Test
    void whenTryToFindNonStoredUserThenReturnNotFound() {
        when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusNotFound()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    @Test
    void whenTryUseNonValidIdThenReturnBadRequest() {
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsBadRequest()
                .assertBody(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(BAD_REQUEST.value());
                });
    }
}
