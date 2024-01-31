package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.OffsetDateTime;

public record QuestionResponse(@JsonProperty("id")
                               @Schema(description = "identificador do usuario",format = "UUID",example = "62bdec5e5a878dfgij898")
                               String id,

                               @JsonProperty("asked")
                               @Schema(description = "pegunta atual do estudo",example = "azul")
                               String asked,
                               @JsonProperty("askedIn")
                               @Schema(description = "quando a pergunta foi gerada",format = "date",example = "blue")
                               OffsetDateTime askedIn) {


    @Builder(toBuilder = true)
    public QuestionResponse {
    }
}
