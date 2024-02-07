package dev.RobertoSimoes.reactiveflashcards.utils.request;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

public class SimpleBodyAsserUtils<B> extends AbstractBodyAsserUtils<B> {
    public SimpleBodyAsserUtils(EntityExchangeResult<B> response) {
        super(response);
    }

}
