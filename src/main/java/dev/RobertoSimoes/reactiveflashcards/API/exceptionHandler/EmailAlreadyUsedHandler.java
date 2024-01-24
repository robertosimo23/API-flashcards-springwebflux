package dev.RobertoSimoes.reactiveflashcards.API.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@Slf4j
public class EmailAlreadyUsedHandler extends AbstractHandlerException<EmailAlreadyUsedException> {
    protected EmailAlreadyUsedHandler(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    Mono<Void> handlerException(ServerWebExchange exchange, EmailAlreadyUsedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return ex.getMessage();
                }).map(message -> buildError(BAD_REQUEST, message))
                .doFirst(() -> log.error("==== EmailAlreadyUsedException", ex))
                .flatMap(response -> writeResponse(exchange, response));

    }
}
