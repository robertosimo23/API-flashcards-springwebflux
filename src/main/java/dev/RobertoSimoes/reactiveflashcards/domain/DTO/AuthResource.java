package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record AuthResource( @JsonProperty("token")
                            String token,
                            @JsonProperty("expiresIn")
                            String expiresIn) {

    @Builder(toBuilder = true)
    public AuthResource {
    }
}
