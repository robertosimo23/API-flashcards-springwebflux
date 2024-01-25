package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import lombok.Builder;

public record StudyRequest(@MongoId
                           @JsonProperty("user.Id")
                           String userId,
                           @MongoId
                           @JsonProperty("deckId")
                           String deckId) {

    @Builder(toBuilder = true)
    public StudyRequest {
    }
}
