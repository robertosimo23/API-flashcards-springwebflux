package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import lombok.Builder;

public record ErrorFieldResponse(String name, String message ) {
    @Builder(toBuilder = true)
    public ErrorFieldResponse { }
}
