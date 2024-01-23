package dev.RobertoSimoes.reactiveflashcards.API.Controller;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.UserResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.UserMapper;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import dev.RobertoSimoes.reactiveflashcards.domain.service.UserService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.UserQueryService;
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
public class UserController {
    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest request) {
        return userService.save(userMapper.toDocument(request))
                .doFirst(() -> log.info("=== Saving a user with follow data {}", request))
                .map(userMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userQueryService.findbyId(id)
                .doFirst(() -> log.info("==== Finding a user with a follow id{}", id))
                .map(userMapper::toResponse);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                     @Valid @RequestBody final UserRequest request) {
        return userService.update(userMapper.toDocument(request, id))
                .doFirst(() -> log.info("==== Updating a user with a follow info [body{}, id: {}", request, id))
                .map(userMapper::toResponse);
    }
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
            public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id){
        return userService.delete(id)
                .doFirst(( ) -> log.info("=== Deleting a user with a follow id{}", id));
    }
}
