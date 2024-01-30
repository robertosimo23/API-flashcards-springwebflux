package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record AuthDTO( @JsonProperty("token")
                            String token,
                            @JsonProperty("expiresIn")
                            String expiresIn) {


}
