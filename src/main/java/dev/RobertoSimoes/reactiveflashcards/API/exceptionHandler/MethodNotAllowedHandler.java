package dev.RobertoSimoes.reactiveflashcards.API.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.GENERIC_METHOD_NOT_ALLOW;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@Slf4j
@Component
public class MethodNotAllowedHandler  extends  AbstractHandlerException<MethodNotAllowedException>{
    public MethodNotAllowedHandler (ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handlerException(ServerWebExchange exchange, MethodNotAllowedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.params(exchange.getRequest().getMethod().name()).getMessage();
                })
                .map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== MethodNotAllowedException: Method [{}] is not allowed at [{}]"
                        , exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), ex))
                .flatMap(response -> writeResponse(exchange, response));

    }
}
