package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record CardRequest(@JsonProperty("front")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "pergunta do card", example = "blue")
                          String front,
                          @JsonProperty("back") @NotBlank
                          @Size
                          @Schema(description = "resposta do card", example = "azul")
                          String back){
    @Builder(toBuilder = true)
    public CardRequest {
    }
}
