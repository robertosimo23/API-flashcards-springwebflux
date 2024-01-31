package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record ErrorFieldResponse(@JsonProperty("name")
                                 @Schema(description = "nome do campo com erro",example = "name")
                                 String name,
                                 @JsonProperty("message")
                                 @Schema(description = "descrição do erro",format = "UUID",example = "o nome deve ter no maximo 255 caracteres")
                                 String message) {
    @Builder(toBuilder = true)
    public ErrorFieldResponse {
    }
}
