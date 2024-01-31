package dev.RobertoSimoes.reactiveflashcards.API.Controller.documentation;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserPageRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.UserPageResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.UserResponse;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name ="Customer" ,description = "Endpoints para manipulação de usuários" )
public interface UserControllerDoc {
    @Operation(summary = "Endopoint para criar um novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar o usuário criado",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = UserResponse.class))})})
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<UserResponse> save(@Valid @RequestBody UserRequest request);

    @Operation(summary = "Endpoint para buscar um usuário pelo seu identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar o usuário correspondente ao identificador",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = UserResponse.class))})})
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> findById(@Parameter(description = "identificador do usuário")
                                @PathVariable @Valid @MongoId(message = "{userController.id}") String id);

    @Operation(summary = "Endpoint para buscar usuários de forma paginada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar os usuários de acordo com as informações passadas na request",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = UserPageResponse.class))})})
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Mono<UserPageResponse> findOnDemand(@Valid UserPageRequest request);

    @Operation(summary = "Endpoint para atualizar um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar o usuário atualizado",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = UserResponse.class))})})
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> update(@Parameter(description = "identificador do usuário")
                              @PathVariable @Valid @MongoId(message = "{userController.id}") String id,
                              @Valid @RequestBody UserRequest request);

    @Operation(summary = "Endpoint para excluir um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "o usuaario foi excluido")})
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@Parameter(description = "identificador do usuário")
                    @PathVariable @Valid @MongoId(message = "{userController.id}") String id);
}
