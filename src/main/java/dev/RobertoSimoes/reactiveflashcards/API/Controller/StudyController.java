package dev.RobertoSimoes.reactiveflashcards.API.Controller;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.AnswerQuestionRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.StudyRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.AnswerQuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.QuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.StudyMapper;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import dev.RobertoSimoes.reactiveflashcards.domain.service.StudyService;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.StudyQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("studies")
@Slf4j
@AllArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final StudyMapper studymapper;
    private final StudyQueryService studyQueryService;
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> star(@Valid @RequestBody final StudyRequest request){
        return studyService.star(studymapper.toDocument(request))
                .doFirst(()-> log.info("=== try to create a study with follow a request {}",request))
                .map(document -> studymapper.toResponse(document.getLastPendingQuestion(), document.id()));
    }
    @GetMapping(produces = APPLICATION_JSON_VALUE,value = "{id}")
    public Mono<QuestionResponse> getCurrentQuestion(@Valid @PathVariable @MongoId(message = "{}")final String id){
        return studyQueryService.getLastPendingQuestion(id)
                .doFirst(() -> log.info("==== try o get a next question in study {}",id ))
                .map(question -> studymapper.toResponse(question,id));
    }
    @PostMapping(produces = APPLICATION_JSON_VALUE,consumes = APPLICATION_JSON_VALUE,value = "{id}/answer")
    public Mono<AnswerQuestionResponse> answer(@Valid @PathVariable @MongoId(message = "{studyController.id}") final String id,
                                                @Valid @RequestBody final AnswerQuestionRequest request ){

        return studyService.answer(id, request.answer())
                .doFirst(()-> log.info ("=== tru to answer pending question is study {} with {}",id ,request.answer()))
                .map(document -> studymapper.toResponse(document.getLastPendingQuestion()));

    }
}
