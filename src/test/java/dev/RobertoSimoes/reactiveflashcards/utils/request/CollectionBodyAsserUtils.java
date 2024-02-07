package dev.RobertoSimoes.reactiveflashcards.utils.request;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

public class CollectionBodyAsserUtils<B> extends AbstractBodyAsserUtils<List<B>> {
    public CollectionBodyAsserUtils(EntityExchangeResult<List<B>> response) {
        super(response);
    }
}
