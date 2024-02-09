package dev.RobertoSimoes.reactiveflashcards.utils.request;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
public class AbstractBodyAssertUtils<B> {
    private final EntityExchangeResult<B> response;

    public B getBody() {
        return response.getResponseBody();
    }

    public void assertBody(final Consumer<B> consumer){
        consumer.accept(response.getResponseBody());
    }
    public AbstractBodyAssertUtils<B> httpStatusIsOk(){
        assertThat(response.getStatus()).isEqualTo(OK);
        return this;
    }
    public AbstractBodyAssertUtils<B> httpStatusIsCreated(){
        assertThat(response.getStatus()).isEqualTo(CREATED);
        return this;
    }
    public void httpStatusIsNoContent(){
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT);
    }
    public AbstractBodyAssertUtils<B> httpStatusIsBadRequest(){
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        return this;
    }
    public AbstractBodyAssertUtils<B> httpStatusNotFound(){
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
        return this;
    }




}
