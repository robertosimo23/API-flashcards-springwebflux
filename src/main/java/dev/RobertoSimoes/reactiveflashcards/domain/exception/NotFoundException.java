package dev.RobertoSimoes.reactiveflashcards.domain.exception;

public class NotFoundException extends ReactiveFlashCardsException {
    public NotFoundException(final String message) {
        super(message);
    }
}
