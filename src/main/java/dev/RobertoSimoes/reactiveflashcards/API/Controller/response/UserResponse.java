package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record UserResponse(@JsonProperty("id")
                           @Schema(description = "identificador do usuario",format = "UUID",example = "62bdec5e5a878dfgij898")
                           String id,
                           @JsonProperty("name")
                           @Schema(description = "nome do usuário", example = "João")
                           String name,
                           @JsonProperty("email")
                           @Schema(description = "email do usuário", example = "joao@joao.com.br")
                           String email) {

    @Builder(toBuilder = true)
    public UserResponse { }
}
