package dev.RobertoSimoes.reactiveflashcards.API.Controller;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.documentation.UserControllerDoc;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserPageRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.UserPageResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.UserResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.UserMapper;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import dev.RobertoSimoes.reactiveflashcards.domain.service.UserService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.UserQueryService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserController implements UserControllerDoc {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar o usuário criado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})})
    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest request) {
        return userService.save(userMapper.toDocument(request))
                .doFirst(() -> log.info("=== Saving a user with follow data {}", request))
                .map(userMapper::toResponse);
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userQueryService.findById(id)
                .doFirst(() -> log.info("==== Finding a user with a follow id{}", id))
                .map(userMapper::toResponse);
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<UserPageResponse> findOnDemand(@Valid final UserPageRequest request) {
        return userQueryService.findOnDemand(request)
                .doFirst(() -> log.info("==== Finding users on demand with follow request {}", request))
                .map(page-> userMapper.toResponse(page, request.limit()));
    }

    @Override
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                     @Valid @RequestBody final UserRequest request) {
        return userService.update(userMapper.toDocument(request, id))
                .doFirst(() -> log.info("==== Updating a user with a follow info [body{}, id: {}", request, id))
                .map(userMapper::toResponse);
    }

    @Override
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userService.delete(id)
                .doFirst(() -> log.info("=== Deleting a user with a follow id{}", id));
    }
}
