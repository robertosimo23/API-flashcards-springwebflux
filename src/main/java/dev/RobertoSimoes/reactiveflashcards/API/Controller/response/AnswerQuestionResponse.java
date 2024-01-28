package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.OffsetDateTime;

public record AnswerQuestionResponse(@JsonProperty("asked ")
                                     String asked,
                                     @JsonProperty("askedIn")
                                     OffsetDateTime askedIn,
                                     @JsonProperty(" answered")
                                     String answered,
                                     @JsonProperty("answeredIn")
                                     OffsetDateTime answeredIn,
                                     @JsonProperty("expected")
                                     String expected) {


    @Builder(toBuilder = true)
    public AnswerQuestionResponse {
    }
}
