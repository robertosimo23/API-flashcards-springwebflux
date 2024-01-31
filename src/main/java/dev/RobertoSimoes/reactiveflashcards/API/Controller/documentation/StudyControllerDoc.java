package dev.RobertoSimoes.reactiveflashcards.API.Controller.documentation;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.AnswerQuestionRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.StudyRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.AnswerQuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.DeckResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.QuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Tag(name = "Study", description = "Endpoints para gerenciar estudos")
public interface StudyControllerDoc {

    @Operation(summary = "Inicia o estudo de um  deck")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "O estudo foi criado e retorna a primeira pergunta gerada",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})})
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<QuestionResponse> star(@Valid @RequestBody StudyRequest request);

    @Operation(summary = "Busca a ultima pergunta não respondida")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "Retorna a ultima pergunta que não foi respondida",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})})
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<QuestionResponse> getCurrentQuestion(@Parameter(description = "identificador do estudo") @Valid @PathVariable @MongoId(message = "{}") String id);


    @Operation(summary = "Responde a pergunta atual ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Retorna a pergunta , a resposta fornecida e a resposta esperada ",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AnswerQuestionResponse.class))})})
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "{id}/answer")
    Mono<AnswerQuestionResponse> answer(@Parameter(description = "identificador do estudo")@Valid @PathVariable @MongoId(message = "{studyController.id}") String id,
                                        @Valid @RequestBody AnswerQuestionRequest request);
}
