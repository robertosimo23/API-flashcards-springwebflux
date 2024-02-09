package dev.RobertoSimoes.reactiveflashcards.utils.request;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

public class EmptyBodyAsserUtils extends AbstractBodyAssertUtils<Void> {
    public EmptyBodyAsserUtils(EntityExchangeResult<Void> response) {
        super(response);
    }
}
