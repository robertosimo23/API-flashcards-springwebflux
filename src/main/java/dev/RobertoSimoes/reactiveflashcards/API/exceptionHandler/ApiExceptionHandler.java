package dev.RobertoSimoes.reactiveflashcards.API.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.DeckInStudyException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.ReactiveFlashCardsException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final DeckStudyHandler deckStudyHandler;
    private final EmailAlreadyUsedHandler emailAlreadyUsedHandler;
    private final MethodNotAllowedHandler methodNotAllowedHandler;
    private final NotFoundHandler notFoundHandler;
    private final ConstraintViolationHandler constraintViolationHandler;
    private final WebExchangeBindHandler webExceptionBindHandler;
    private final ResponseStatusHandler responseStatusHandler;
    private final ReactiveFlashCardsHandler reactiveFlashCardsHandler;
    private final Exception_Generic_Handler exception_generic_handler;
    private final JsonProcessingHandler jsonProcessingHandler;


    private final ObjectMapper mapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)

                .onErrorResume(DeckInStudyException.class, e -> deckStudyHandler.handlerException(exchange, e))
                .onErrorResume(EmailAlreadyUsedException.class,e -> emailAlreadyUsedHandler.handlerException(exchange, e))
                .onErrorResume(MethodNotAllowedException.class, e ->
                        methodNotAllowedHandler.handlerException(exchange, e))
                .onErrorResume(NotFoundException.class, e ->
                        notFoundHandler.handlerException(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e ->
                        constraintViolationHandler.handlerException(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e ->
                        webExceptionBindHandler.handlerException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e ->
                        responseStatusHandler.handlerException(exchange, e))
                .onErrorResume(ReactiveFlashCardsException.class, e ->
                        reactiveFlashCardsHandler.handlerException(exchange, e))
                .onErrorResume(Exception.class, e ->
                        exception_generic_handler.handlerException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e ->
                        jsonProcessingHandler.handlerException(exchange, e))
                .then();
    }

}
