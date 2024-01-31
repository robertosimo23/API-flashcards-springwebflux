package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

public record CardResponse(@JsonProperty("front")
                           @Schema(description = "pergunta do card", example = "blue")
                           String front,
                           @JsonProperty("back")
                           @Schema(description = "momento em que a pergunta foi gerada", example = "azul")
                           String back){


    @Builder(toBuilder = true)
    public CardResponse {}
}
