package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ProblemResponse(@JsonProperty("status")
                              @Schema(description = "http status retornado", example = "400")
                              Integer status,
                              @JsonProperty("timestamp")
                              @Schema(description = "momento em que o erro aconteceu ", format = "datetime", example = "")
                              OffsetDateTime timestamp,
                              @JsonProperty("errorDescription")
                              @Schema(description = "descrição do erro", example = "sua requisição tem informações inválidas")
                              String errorDescription,
                              @JsonProperty("fields")
                              @Schema(description = "caso a requisição tenha parametros inválidos aqui serão informados os erros refrentes aos mesmos ", format = "UUID", example = "62bdec5e5a878dfgij898")
                              List<ErrorFieldResponse> fields) {
    @Builder(toBuilder = true)
    public ProblemResponse {
    }
}
