package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record CardDTO( @JsonProperty("ask")String ask,
                       @JsonProperty("answer") String answer){

    @Builder(toBuilder = true)
    public CardDTO{ }

}
