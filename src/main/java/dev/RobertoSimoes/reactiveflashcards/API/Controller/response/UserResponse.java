package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record UserResponse(@JsonProperty("id")
                           String id,
                           @JsonProperty("name")
                           String name,
                           @JsonProperty("email")
                           String email) {

    @Builder(toBuilder = true)
    public UserResponse { }
}
