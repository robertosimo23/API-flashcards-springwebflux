package dev.RobertoSimoes.reactiveflashcards.domain.exception;

public class RetryException extends  ReactiveFlashCardsException{

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
