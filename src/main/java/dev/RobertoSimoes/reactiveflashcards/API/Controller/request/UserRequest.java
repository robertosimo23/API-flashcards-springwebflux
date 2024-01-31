package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record UserRequest(@NotBlank
                          @Size(min = 1, max = 255)
                          @JsonProperty("name")
                          @Schema(description = "nome do usuário", example = "João")
                          String name,
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Email
                          @JsonProperty("email")
                          @Schema(description = "email do usuário", example = "joao@joao.com.br")
                          String email) {

    @Builder(toBuilder = true)
    public UserRequest {
    }
}
