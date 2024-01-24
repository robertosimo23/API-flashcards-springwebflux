package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Set;

public record CardResponse(@JsonProperty("front") String front, @JsonProperty("back") String back){
    @Builder(toBuilder = true)
    public CardResponse {}
}
