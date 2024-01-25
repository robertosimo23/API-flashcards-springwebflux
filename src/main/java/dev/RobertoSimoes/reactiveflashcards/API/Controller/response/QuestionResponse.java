package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.OffsetDateTime;

public record QuestionResponse(@JsonProperty("asked")
                               String asked,
                                @JsonProperty("askedIn")
                               OffsetDateTime askedIn) {




    @Builder(toBuilder = true)
    public QuestionResponse {
    }
}
