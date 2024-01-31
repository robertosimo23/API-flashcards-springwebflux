package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

public record DeckRequest(@JsonProperty("name")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "nome do deck", example = "estudo de inglês")
                          String name,
                          @JsonProperty("description")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "descrição do card", example = "deck de estudo de inglês para iniciantes")
                          String description,
                          @Valid
                          @Size(min = 3)
                          @JsonProperty("cards")
                          @Schema(description = "cards que compõem o deck")
                          Set<CardRequest> cards) {
    @Builder (toBuilder = true)
    public DeckRequest{ }
}
