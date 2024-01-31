package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.RobertoSimoes.reactiveflashcards.core.validation.MongoId;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record StudyRequest(@MongoId
                           @JsonProperty("user.Id")
                           @Schema(description = "identificador de usuario", example = "62weucyhbwqeruybqwrovuybervueroy",format ="UUID" )
                           String userId,
                           @MongoId
                           @JsonProperty("deckId")
                           @Schema(description = "identificador do deck", example = "62weucyhbwqeruybqwrovuybervueroy",format ="UUID")
                           String deckId) {

    @Builder(toBuilder = true)
    public StudyRequest {
    }
}
