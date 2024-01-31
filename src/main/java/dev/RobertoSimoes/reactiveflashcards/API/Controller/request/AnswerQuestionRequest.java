package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Builder(toBuilder = true)
public record AnswerQuestionRequest (@JsonProperty("answer")
                                     @Size(min = 1,max = 255)
                                     @NotBlank
                                     @Schema(description = "resposta da pergunta atual", example = "joao@joao.com.br")
                                     String answer) {
    public AnswerQuestionRequest { }
}
