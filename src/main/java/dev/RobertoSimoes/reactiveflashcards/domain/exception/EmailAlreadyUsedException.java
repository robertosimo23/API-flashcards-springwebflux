package dev.RobertoSimoes.reactiveflashcards.domain.exception;

public class EmailAlreadyUsedException extends ReactiveFlashCardsException{
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
