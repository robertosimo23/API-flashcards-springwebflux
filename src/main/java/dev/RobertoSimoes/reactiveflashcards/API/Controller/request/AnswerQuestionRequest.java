package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record AnswerQuestionRequest (@JsonProperty("answer")
                                    @Size(min = 1,max = 255)
                                    @NotBlank String answer) {
@Builder(toBuilder = true)
    public AnswerQuestionRequest { }
}
