package dev.RobertoSimoes.reactiveflashcards.API.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ErrorFieldResponse;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.ReactiveFlashCardsException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.*;
import static jdk.internal.joptsimple.internal.Messages.message;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class apiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(NotFoundException.class, e -> handleNotFoundException(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e -> handleConstraintViolationException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> handleResponseStatusException(exchange, e))
                .onErrorResume(ReactiveFlashCardsException.class, e -> handleReactiveFlashCardsException(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e -> handleWebExchangeBindException(exchange, e))
                .onErrorResume(Exception.class, e -> handleException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> handleJsonProcessingException(exchange, e))
                .then();
    }

    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange, final MethodNotAllowedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.params(exchange.getRequest().getMethod().name()).getMessage();
                })
                .map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== MethodNotAllowedException: Method [{}] is not allowed at [{}]"
                 , exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), ex))
                .flatMap(response -> writeResponse(exchange, response));

    }

    private Mono<Void> handleNotFoundException(final ServerWebExchange exchange, final NotFoundException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, NOT_FOUND);
                    return ex.getMessage();
                }).map(message -> buildError(NOT_FOUND, message))
                .doFirst(() -> log.error("==== NotFoundException", ex))
                .flatMap(response -> writeResponse(exchange, response));

    }

    private Mono<Void> handleConstraintViolationException(final ServerWebExchange exchange, final ConstraintViolationException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                }).map(message -> buildError(BAD_REQUEST, message))
                .flatMap(response -> buildParamErrorMessage(response, ex))
                .doFirst(() -> log.error("==== ConstraintViolationException: ", ex))
                .flatMap(response -> writeResponse(exchange, response));

    }

    private Mono<ProblemResponse> buildParamErrorMessage(final ProblemResponse response, final ConstraintViolationException ex) {
        return Flux.fromIterable(ex.getConstraintViolations())
                .map(constraintViolation -> ErrorFieldResponse.builder()
                        .name(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().toString())
                        .message(constraintViolation.getMessage()).build())
                .collectList()
                .map(problems -> response.toBuilder().fields(problems).build());

    }

    private Mono<Void> handleResponseStatusException(final ServerWebExchange exchange, final ResponseStatusException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, NOT_FOUND);
                    return GENERIC_NOT_FOUND.getMessage();
                }).map(message -> buildError(NOT_FOUND, message))
                .doFirst(() -> log.error("==== ResponseStatusException: ", ex))
                .flatMap(response -> writeResponse(exchange, response));

    }

    private Mono<Void> handleWebExchangeBindException(final ServerWebExchange exchange, final WebExchangeBindException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                }).map(message -> buildError(BAD_REQUEST, message))
                .doFirst(() -> log.error("==== WebExchangeBindException: ", ex))
                .flatMap(response -> writeResponse(exchange, response));

    }

    private Mono<ProblemResponse> buildParamErrorMessage(final ProblemResponse response, final WebExchangeBindException ex) {
        return Flux.fromIterable(ex.getAllErrors())
               .map(objectError -> ErrorFieldResponse.builder()
               .name(objectError instanceof FieldError fieldError ? fieldError.getField():objectError.getObjectName())
               .message(messageSource.getMessage(objectError, LocaleContextHolder.getLocale())).build())
               .collectList()
               .map(problems -> response.toBuilder().fields(problems).build());

    }

    private Mono<Void> handleReactiveFlashCardsException(final ServerWebExchange exchange, final ReactiveFlashCardsException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> buildError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== ReactiveFlashCardsException", ex))
                .flatMap(response -> writeResponse(exchange, response));

    }


    private Mono<Void> handleException(final ServerWebExchange exchange, final Exception ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> buildError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== Exception", ex))
                .flatMap(response -> writeResponse(exchange, response));

    }

    private Mono<Void> handleJsonProcessingException(final ServerWebExchange exchange, final JsonProcessingException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                }).map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== JsonProcessingException: Failed to map exception for the request {}", exchange.getRequest().getPath().value(), ex))
                .flatMap(response -> writeResponse(exchange, response));

    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ProblemResponse response) {
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
