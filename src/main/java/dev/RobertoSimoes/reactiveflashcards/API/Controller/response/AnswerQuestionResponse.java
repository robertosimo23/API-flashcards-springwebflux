package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.OffsetDateTime;

public record AnswerQuestionResponse(@JsonProperty("asked ")
                                     @Schema(description = "pergunta feita", example = "João")
                                     String asked,
                                     @JsonProperty("askedIn")
                                     @Schema(description = "momento em que a pergunta foi gerada", format = "datetime", example = "1")
                                     OffsetDateTime askedIn,
                                     @JsonProperty(" answered")
                                     @Schema(description = "resposta fornecida", example = "azul")
                                     String answered,
                                     @JsonProperty("answeredIn")
                                     @Schema(description = "momento em que a pergunta foi respondida", format = "datetime", example = "1")

                                     OffsetDateTime answeredIn,
                                     @JsonProperty("expected")
                                     @Schema(description = "resposta esperada", format = "datetime", example = "azul")
                                     String expected) {


    @Builder(toBuilder = true)
    public AnswerQuestionResponse {
    }
}
