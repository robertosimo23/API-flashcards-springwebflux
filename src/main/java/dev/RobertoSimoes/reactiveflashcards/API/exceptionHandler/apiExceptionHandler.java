package dev.RobertoSimoes.reactiveflashcards.API.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.ReactiveFlashCardsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.GENERIC_EXCEPTION;
import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.GENERIC_METHOD_NOT_ALLOW;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class apiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(ReactiveFlashCardsException.class, e -> handleReactiveFlashCardsException(exchange, e))
                .onErrorResume(Exception.class, e -> handleException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e-> handleJsonProcessingException(exchange,e))
                .then();
    }

    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange, final MethodNotAllowedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                }).map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== MethodNotAllowedException: Method [{}] is not allowed at [{}]"
                        , exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), ex))
                .flatMap(response -> writeREsponse(exchange, response));

    }


    private Mono<Void> handleReactiveFlashCardsException(final ServerWebExchange exchange, final ReactiveFlashCardsException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> buildError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== ReactiveFlashCardsException", ex))
                .flatMap(response -> writeREsponse(exchange, response));

    }


    private Mono<Void> handleException(final ServerWebExchange exchange, final Exception ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> buildError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== Exception", ex))
                .flatMap(response -> writeREsponse(exchange, response));

    }

    private Mono<Void> handleJsonProcessingException(final ServerWebExchange exchange, final JsonProcessingException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                }).map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== JsonProcessingException: Failed to map exception for the request {}", exchange.getRequest().getMethod(), ex))
                .flatMap(response -> writeREsponse(exchange, response));

    }

    private Mono<Void> writeREsponse(ServerWebExchange exchange, ProblemResponse response) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(response))));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode) {
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);

    }

    private ProblemResponse buildError(final HttpStatus status, final String errorDescription) {
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDescription)
                .build();
    }
}
