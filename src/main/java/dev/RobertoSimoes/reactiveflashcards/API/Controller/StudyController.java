package dev.RobertoSimoes.reactiveflashcards.API.Controller;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.StudyRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.QuestionResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Mapper.StudyMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.service.StudyService;
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
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> star(@Valid @RequestBody final StudyRequest request){
        return studyService.star(studymapper.toDocument(request))
                .doFirst(()-> log.info("=== try to create a study with follow a request {}",request))
                .map(document -> studymapper.toResponse(document.getLastQuestionPending()));
    }
}
