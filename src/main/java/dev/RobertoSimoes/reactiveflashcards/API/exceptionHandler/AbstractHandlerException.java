package dev.RobertoSimoes.reactiveflashcards.API.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.ProblemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.PROTECTED;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@RequiredArgsConstructor(access = PROTECTED)
public abstract class AbstractHandlerException<T extends Exception> {

    private final ObjectMapper mapper;

    abstract Mono<Void> handlerException(final ServerWebExchange exchange, final T ex);

    protected ProblemResponse buildError(final HttpStatus status, final String errorDescription) {
        return ProblemResponse.builder().status(status.value()).errorDescription(errorDescription)
                .timestamp(OffsetDateTime.now())
                .build();
    }

    protected Mono<Void> writeResponse(ServerWebExchange exchange, ProblemResponse response) {
        return exchange.getResponse().writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(response))));
    }

    protected void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode) {
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);

    }
}
