package dev.RobertoSimoes.reactiveflashcards.utils.request;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
public class AbstractBodyAsserUtils<B> {
    private final EntityExchangeResult<B> response;

    public B getBody() {
        return response.getResponseBody();
    }

    public AbstractBodyAsserUtils<B> assertBody(final Consumer<B> consumer) {
        consumer.accept(response.getResponseBody());
        return this;
    }
    public AbstractBodyAsserUtils<B> HttpStatusIsOk(){
        assertThat(response.getStatus()).isEqualTo(OK);
        return this;
    }
    public AbstractBodyAsserUtils<B> HttpStatusIsCreated(){
        assertThat(response.getStatus()).isEqualTo(CREATED);
        return this;
    }
    public AbstractBodyAsserUtils<B> HttpStatusIsNoContent(){
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT);
        return this;
    }
    public AbstractBodyAsserUtils<B> HttpStatusIsBadRequest(){
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        return this;
    }




}
