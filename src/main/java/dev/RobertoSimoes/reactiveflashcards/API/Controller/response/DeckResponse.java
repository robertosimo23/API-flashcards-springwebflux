package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

public record DeckResponse(@JsonProperty("id")
                           @Schema(description = "identificador do deck", format = "UUID", example = "62bdec5e5a878dfgij898")
                           String id,
                           @JsonProperty("name")
                           @Schema(description = "nome do deck", example = "estudo de inglês para iniciantes")
                           String name,
                           @JsonProperty("description")
                           @Schema(description = "descrição do deck", example = "deck de |estudo de inglês para iniciantes")
                           String description,
                           @JsonProperty("cards")
                           @Schema(description = "cards que compõem o deck")
                           Set<CardResponse> cards) {
    @Builder(toBuilder = true)
    public DeckResponse {
    }
}
